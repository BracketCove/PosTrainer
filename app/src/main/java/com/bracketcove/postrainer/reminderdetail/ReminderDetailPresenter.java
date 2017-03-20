package com.bracketcove.postrainer.reminderdetail;


import com.bracketcove.postrainer.R;
import com.bracketcove.postrainer.data.reminder.Reminder;
import com.bracketcove.postrainer.data.reminder.ReminderSource;
import com.bracketcove.postrainer.util.BaseSchedulerProvider;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableSingleObserver;

/**
 * Created by Ryan on 05/03/2017.
 */

public class ReminderDetailPresenter implements ReminderDetailContract.Presenter {

    private final ReminderDetailContract.View view;
    private final ReminderSource reminderSource;
    private final BaseSchedulerProvider schedulerProvider;
    private final CompositeDisposable compositeDisposable;

    @Inject
    public ReminderDetailPresenter(ReminderDetailContract.View view,
                                   ReminderSource reminderSource,
                                   BaseSchedulerProvider schedulerProvider) {
        this.view = view;
        this.reminderSource = reminderSource;
        this.schedulerProvider = schedulerProvider;
        this.compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void subscribe() {
        compositeDisposable.add(
              reminderSource.getReminderById(
                      view.getReminderId())
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribeWith(new DisposableSingleObserver<Reminder>() {
                    @Override
                    public void onSuccess(Reminder reminder) {
                        view.setReminderTitle(reminder.getReminderTitle());
                        view.setVibrateOnly(reminder.isVibrateOnly());
                        view.setRenewAutomatically(reminder.isRenewAutomatically());
                        view.setPickerTime(reminder.getHourOfDay(), reminder.getMinute());
                        view.setCurrentAlarmState(reminder.isActive());
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.makeToast(R.string.error_invalid_reminder_id);
                        view.startReminderListActivity();
                    }
                })
        );
    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public void onBackIconPress() {
        view.startReminderListActivity();
    }

    /**
     * Ensure that the Reminder is updated in repository
     *
     */
    @Override
    public void onDoneIconPress() {
        Reminder reminder = new Reminder(
                view.getPickerHour(),
                view.getPickerMinute(),
                view.getReminderTitle(),
                view.getCurrentAlarmState(),
                view.getVibrateOnly(),
                view.getRenewAutomatically(),
                view.getReminderId()
        );

        compositeDisposable.add(
                reminderSource.updateReminder(reminder)
                        .subscribeOn(schedulerProvider.io())
                        .observeOn(schedulerProvider.ui())
                        .subscribeWith(new DisposableCompletableObserver() {
                            @Override
                            public void onComplete() {
                                view.makeToast(R.string.message_database_write_successful);
                                view.startReminderListActivity();
                            }

                            @Override
                            public void onError(Throwable e) {
                                view.makeToast(R.string.error_database_write_failure);

                            }
                        })
        );
    }
}
