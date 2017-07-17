package com.bracketcove.postrainer.reminderlist;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;

import com.bracketcove.postrainer.R;
import com.bracketcove.postrainer.data.alarm.AlarmService;
import com.bracketcove.postrainer.data.reminder.ReminderService;
import com.bracketcove.postrainer.data.viewmodel.Reminder;
import com.bracketcove.postrainer.data.viewmodel.ReminderListViewModel;
import com.bracketcove.postrainer.usecase.CancelAlarm;
import com.bracketcove.postrainer.usecase.DeleteReminder;
import com.bracketcove.postrainer.usecase.GetReminderList;
import com.bracketcove.postrainer.usecase.SetAlarm;
import com.bracketcove.postrainer.usecase.UpdateOrCreateReminder;
import com.bracketcove.postrainer.util.BaseSchedulerProvider;

import org.reactivestreams.Subscriber;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * Retrieves a List of any Reminders which are Present in ReminderService (Realm Database), and
 * displays passes them to the View.
 * Created by Ryan on 05/03/2017.
 */

public class ReminderListPresenter implements ReminderListContract.Presenter {

    private final ReminderListContract.View view;
    private final BaseSchedulerProvider schedulerProvider;
    private final CompositeDisposable compositeDisposable;

    //Use Cases
    private final GetReminderList getReminderList;
    private final UpdateOrCreateReminder updateOrCreateReminder;
    private final DeleteReminder deleteReminder;
    private final SetAlarm setAlarm;
    private final CancelAlarm cancelAlarm;

    //ViewModel
    private ReminderListViewModel viewModel;

    @Inject
    public ReminderListPresenter(ReminderListContract.View view,
                                 ReminderService reminderService,
                                 AlarmService alarmService,
                                 BaseSchedulerProvider schedulerProvider) {

        this.getReminderList = new GetReminderList(reminderService);
        this.updateOrCreateReminder = new UpdateOrCreateReminder(reminderService);
        this.deleteReminder = new DeleteReminder(reminderService);
        this.setAlarm = new SetAlarm(alarmService);
        this.cancelAlarm = new CancelAlarm(alarmService);

        this.view = view;
        this.schedulerProvider = schedulerProvider;
        this.compositeDisposable = new CompositeDisposable();


    }

    @Inject
    void setPresenter() {
        view.setPresenter(this);
    }


    @Override
    public void start() {
        getReminders();
    }

    @Override
    public void stop() {
        compositeDisposable.clear();
    }

    /**
     * Checks Repository for any existing reminders.
     * returns one of:
     * List of 1-5 Reminders : Display Reminders to User
     * Nothing : Display create RealmReminder Prompt to User
     * error : Display database error
     */
    private void getReminders() {
        compositeDisposable.add(
                getReminderList.runUseCase()
                        .subscribeOn(schedulerProvider.io())
                        .observeOn(schedulerProvider.ui())
                        .subscribeWith(
                                new DisposableSubscriber<List<Reminder>>() {
                                    @Override
                                    public void onNext(List<Reminder> reminders) {
                                        view.setReminderListData(reminders);
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        view.makeToast(R.string.error_database_connection_failure);

                                    }

                                    @Override
                                    public void onComplete() {
                                        view.setNoReminderListDataFound();
                                    }
                                }
                        )
        );


    }


    /**
     * Compares a desired reminder to the state of the current reminder.
     * - If states matches, we shouldn't need to update the Repository (I think so anyway).
     * - If state doesn't match, animate the toggle in the view, and update the Repository to
     * desired state
     *
     * @param active   user's desired state of the alarm (probably)
     * @param reminder current state of alarm in View. Assumed to be current with Repository
     */
    @Override
    public void onReminderToggled(final boolean active, final Reminder reminder) {
        if (active != reminder.isActive()) {
            reminder.setActive(active);

            compositeDisposable.add(
                    updateOrCreateReminder.runUseCase(reminder)
                            .subscribeOn(schedulerProvider.io())
                            .observeOn(schedulerProvider.ui())
                            .subscribeWith(
                                    new DisposableCompletableObserver() {
                                        @Override
                                        public void onError(Throwable e) {
                                            view.makeToast(R.string.error_database_write_failure);
                                        }

                                        @Override
                                        public void onComplete() {
                                            if (active) {
                                                onAlarmSet(reminder);
                                            } else {
                                                onAlarmCancelled(reminder);
                                            }
                                        }
                                    }
                            )
            );

        } else {
            view.makeToast(getAppropriateMessage(active));
        }
    }

    private int getAppropriateMessage(boolean active) {
        if (active) {
            return R.string.msg_alarm_activated;
        } else {
            return R.string.msg_alarm_deactivated;
        }
    }

    private void onAlarmSet(Reminder reminder) {
        compositeDisposable.add(
                setAlarm.runUseCase(reminder)
                        .subscribeOn(schedulerProvider.io())
                        .observeOn(schedulerProvider.ui())
                        .subscribeWith(
                                new DisposableCompletableObserver() {
                                    @Override
                                    public void onError(Throwable e) {
                                        view.makeToast(R.string.error_managing_alarm);
                                    }

                                    @Override
                                    public void onComplete() {
                                        view.makeToast(R.string.msg_alarm_activated);
                                    }
                                }
                        )
        );


    }

    private void onAlarmCancelled(Reminder reminder) {
        compositeDisposable.add(
                cancelAlarm.runUseCase(reminder)
                        .subscribeOn(schedulerProvider.io())
                        .observeOn(schedulerProvider.ui())
                        .subscribeWith(
                                new DisposableCompletableObserver() {
                                    @Override
                                    public void onError(Throwable e) {
                                        view.makeToast(R.string.error_managing_alarm);
                                    }

                                    @Override
                                    public void onComplete() {
                                        view.makeToast(R.string.msg_alarm_deactivated);
                                    }
                                }
                        )
        );
    }


    @Override
    public void onSettingsIconClick() {
        view.startSettingsActivity();
    }

    @Override
    public void onReminderSwiped(final int position, final Reminder reminder) {
        compositeDisposable.add(
                deleteReminder.runUseCase(reminder)
                        .subscribeOn(schedulerProvider.io())
                        .observeOn(schedulerProvider.ui())
                        .subscribeWith(new DisposableCompletableObserver() {
                            @Override
                            public void onComplete() {
                                view.makeToast(R.string.msg_alarm_deleted);
                            }

                            @Override
                            public void onError(Throwable e) {
                                view.makeToast(R.string.error_database_connection_failure);
                                view.undoDeleteReminderAt(position, reminder);
                            }
                        })
        );
    }

    @Override
    public void onReminderIconClick(Reminder reminder) {
        view.startReminderDetailActivity(reminder.getReminderId());
    }

    @Override
    public void onCreateReminderButtonClick(int currentNumberOfReminders,
                                            Reminder reminder) {

        //only allow up to 5 Reminders at a time
        if (currentNumberOfReminders < 5) {
            compositeDisposable.add(
                    updateOrCreateReminder.runUseCase(reminder)
                            .subscribeOn(schedulerProvider.io())
                            .observeOn(schedulerProvider.ui())
                            .subscribeWith(
                                    new DisposableCompletableObserver() {
                                        @Override
                                        public void onComplete() {
                                            getReminders();
                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            view.makeToast(R.string.error_database_write_failure);
                                        }
                                    })
            );

        } else {
            view.makeToast(R.string.msg_reminder_limit_reached);
        }
    }
}
