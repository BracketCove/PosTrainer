package com.bracketcove.postrainer.reminderdetail;


import com.bracketcove.postrainer.R;
import com.bracketcove.postrainer.data.reminder.ReminderService;
import com.bracketcove.postrainer.data.viewmodel.Reminder;
import com.bracketcove.postrainer.usecase.GetReminder;
import com.bracketcove.postrainer.usecase.UpdateOrCreateReminder;
import com.bracketcove.postrainer.util.BaseSchedulerProvider;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by Ryan on 05/03/2017.
 */

public class ReminderDetailPresenter implements ReminderDetailContract.Presenter {

    //Use Cases
    private final GetReminder getReminder;
    private final UpdateOrCreateReminder updateOrCreateReminder;


    private final ReminderDetailContract.View view;
    private final BaseSchedulerProvider schedulerProvider;
    private final CompositeDisposable compositeDisposable;

    //Constructor Injection: What is a use case for Constructor Injection?
    @Inject
    public ReminderDetailPresenter(ReminderDetailContract.View view,
                                   ReminderService reminderService,
                                   BaseSchedulerProvider schedulerProvider) {
        this.getReminder = new GetReminder(reminderService);
        this.updateOrCreateReminder = new UpdateOrCreateReminder(reminderService);

        this.view = view;
        this.schedulerProvider = schedulerProvider;
        this.compositeDisposable = new CompositeDisposable();
    }

    //Method Injection
    @Inject
    void setPresenter() {
        view.setPresenter(this);
    }

    @Override
    public void subscribe() {
        Reminder reminder = view.getViewModel();
        reminder.setReminderId(view.getReminderId());

        getReminder.runUseCase(reminder)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribeWith(
                        new DisposableObserver<Reminder>() {
                            @Override
                            public void onNext(Reminder reminder) {
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

                            @Override
                            public void onComplete() {

                            }
                        });
    }

    @Override
    public void unSubscribe() {

    }

    @Override
    public void onBackIconPress() {
        view.startReminderListActivity();
    }

    /**
     * Ensure that the RealmReminder is updated in repository
     */
    @Override
    public void onDoneIconPress() {
        Reminder reminder = view.getViewModel();
        reminder.setReminderId(view.getReminderId());
        updateOrCreateReminder.runUseCase(reminder)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribeWith(
                        new DisposableObserver() {
                            @Override
                            public void onComplete() {
                                view.makeToast(R.string.message_database_write_successful);
                                view.startReminderListActivity();
                            }

                            @Override
                            public void onNext(Object o) {

                            }

                            @Override
                            public void onError(Throwable e) {
                                view.makeToast(R.string.error_database_write_failure);
                            }
                        });
    }
}
