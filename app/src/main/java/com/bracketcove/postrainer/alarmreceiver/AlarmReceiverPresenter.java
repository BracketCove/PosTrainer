package com.bracketcove.postrainer.alarmreceiver;

import android.util.Log;

import com.bracketcove.postrainer.R;
import com.bracketcove.postrainer.data.alarm.AlarmService;
import com.bracketcove.postrainer.data.alarm.AlarmSource;
import com.bracketcove.postrainer.data.reminder.ReminderService;
import com.bracketcove.postrainer.data.reminder.ReminderSource;
import com.bracketcove.postrainer.data.viewmodel.Reminder;
import com.bracketcove.postrainer.usecase.DismissAlarm;
import com.bracketcove.postrainer.usecase.GetReminder;
import com.bracketcove.postrainer.usecase.StartAlarm;
import com.bracketcove.postrainer.util.BaseSchedulerProvider;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;

/**
 * Created by Ryan on 05/03/2017.
 */

public class AlarmReceiverPresenter implements AlarmReceiverContract.Presenter {

    private final DismissAlarm dismissAlarm;
    private final StartAlarm startAlarm;
    private final GetReminder getReminder;

    private final AlarmReceiverContract.View view;
    private final BaseSchedulerProvider schedulerProvider;
    private final CompositeDisposable compositeDisposable;
    private Reminder currentReminder;

    @Inject
    public AlarmReceiverPresenter(AlarmReceiverContract.View view,
                                  ReminderService reminderService,
                                  AlarmService alarmService,
                                  BaseSchedulerProvider schedulerProvider) {
        this.getReminder = new GetReminder(reminderService);
        this.dismissAlarm = new DismissAlarm(alarmService);
        this.startAlarm = new StartAlarm(alarmService);

        this.view = view;
        this.schedulerProvider = schedulerProvider;
        this.compositeDisposable = new CompositeDisposable();
    }

    @Inject
    void setPresenter() {
        view.setPresenter(this);
    }


    @Override
    public void subscribe() {
        getReminderFromDatabase();
    }

    /**
     * Query the Reminder Database for a Reminder which matches the given reminderId passed
     * in from the Activity's extras.
     */
    private void getReminderFromDatabase() {
        Reminder reminder = new Reminder();
        reminder.setReminderId(view.getReminderId());

        getReminder.runUseCase(reminder)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribeWith(new DisposableObserver<Reminder>() {

                    @Override
                    public void onNext(Reminder reminder) {
                        startAlarm(reminder);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("REMINDER_DB", e.getMessage().toString());
                        view.makeToast(R.string.error_database_connection_failure);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void startAlarm(Reminder reminder) {
        startAlarm.runUseCase(reminder)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        //Probably don't need to do anything here
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("ALARMRECEIVER", e.getMessage().toString());
                        view.makeToast(R.string.error_starting_alarm);
                    }
                });
    }

    @Override
    public void unSubscribe() {
        compositeDisposable.clear();
    }

    @Override
    public void onAlarmDismissClick() {
        //first, stop the media player and vibrator
        dismissAlarm.runUseCase(new Reminder())
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        //TODO figure out what to do here
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("ALARM", e.getMessage().toString());
                    }
                });
    }
}
