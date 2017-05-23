package com.bracketcove.postrainer.alarmreceiver;

import android.util.Log;
import android.widget.Toast;

import com.bracketcove.postrainer.R;
import com.bracketcove.postrainer.data.alarm.AlarmSource;
import com.bracketcove.postrainer.data.reminder.ReminderSource;
import com.bracketcove.postrainer.data.viewmodel.Reminder;
import com.bracketcove.postrainer.util.BaseSchedulerProvider;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableSingleObserver;

/**
 * Created by Ryan on 05/03/2017.
 */

public class AlarmReceiverPresenter implements AlarmReceiverContract.Presenter {

    //TODO refactor to Inject Use Cases instead of Data Sources (AlarmReceiver)
    private final AlarmReceiverContract.View view;
    private final ReminderSource reminderSource;
    private final AlarmSource alarmSource;
    private final BaseSchedulerProvider schedulerProvider;
    private final CompositeDisposable compositeDisposable;
    private Reminder currentReminder;

    @Inject
    public AlarmReceiverPresenter(AlarmReceiverContract.View view,
                                  ReminderSource reminderSource,
                                  AlarmSource alarmSource,
                                  BaseSchedulerProvider schedulerProvider) {
        this.view = view;
        this.reminderSource = reminderSource;
        this.alarmSource = alarmSource;
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
        compositeDisposable.add(
                reminderSource.getReminderById(view.getReminderId())
                        .subscribeOn(schedulerProvider.io())
                        .observeOn(schedulerProvider.ui())
                        .subscribeWith(new DisposableSingleObserver<Reminder>() {
                            @Override
                            public void onSuccess(Reminder reminder) {
                                startAlarm(reminder);
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d("REMINDER_DB", e.getMessage().toString());
                                view.makeToast(R.string.error_database_connection_failure);
                            }
                        })
        );
    }

    private void startAlarm(Reminder reminder) {
        compositeDisposable.add(
                alarmSource.startAlarm(reminder)
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
                        })
        );
    }

    @Override
    public void unSubscribe() {
        compositeDisposable.clear();
    }

    @Override
    public void onAlarmDismissClick() {
        //first, stop the media player and vibrator
        compositeDisposable.add(
                alarmSource.stopMediaPlayerAndVibrator()
                        .subscribeOn(schedulerProvider.io())
                        .observeOn(schedulerProvider.ui())
                        .subscribeWith(new DisposableCompletableObserver() {
                            @Override
                            public void onComplete() {
                                releaseWakeLock();
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d("ALARM", e.getMessage().toString());
                                releaseWakeLock();
                            }
                        })
        );
    }

    private void releaseWakeLock() {
        compositeDisposable.add(
                alarmSource.releaseWakeLock()
                        .subscribeOn(schedulerProvider.io())
                        .observeOn(schedulerProvider.ui())
                        .subscribeWith(new DisposableCompletableObserver() {
                            @Override
                            public void onComplete() {
                                view.finishActivity();
                            }

                            @Override
                            public void onError(Throwable e) {
                                //not really sure what else to do here. If I can't release the
                                //wakelock, I'll just assume it's null.
                                view.finishActivity();
                            }
                        })
        );
    }

}
