package com.bracketcove.postrainer.reminderlist;

import com.bracketcove.postrainer.R;
import com.bracketcove.postrainer.data.alarm.AlarmSource;
import com.bracketcove.postrainer.data.reminder.RealmReminder;
import com.bracketcove.postrainer.data.reminder.ReminderSource;
import com.bracketcove.postrainer.util.BaseSchedulerProvider;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableMaybeObserver;

/**
 * Created by Ryan on 05/03/2017.
 */

public class ReminderListPresenter implements ReminderListContract.Presenter {

    private final ReminderListContract.View view;
    private final ReminderSource reminderSource;
    private final AlarmSource alarmSource;
    private final BaseSchedulerProvider schedulerProvider;
    private final CompositeDisposable compositeDisposable;

    @Inject
    public ReminderListPresenter(ReminderListContract.View view,
                                 ReminderSource reminderSource,
                                 AlarmSource alarmSource,
                                 BaseSchedulerProvider schedulerProvider) {
        this.view = view;
        this.reminderSource = reminderSource;
        this.alarmSource = alarmSource;
        this.schedulerProvider = schedulerProvider;
        this.compositeDisposable = new CompositeDisposable();

    }

    /**
     * Checks Repository for any existing reminders.
     * returns one of:
     * List of 1-5 Reminders : Display Reminders to User
     * Nothing : Display create RealmReminder Prompt to User
     * error : Display database error
     */
    @Override
    public void subscribe() {
        compositeDisposable.add(
                reminderSource.getReminders()
                        .subscribeOn(schedulerProvider.io())
                        .observeOn(schedulerProvider.ui())
                        .subscribeWith(new DisposableMaybeObserver<List<RealmReminder>>() {
                            @Override
                            public void onSuccess(List<RealmReminder> reminders) {
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
                        })
        );
    }

    @Override
    public void unsubscribe() {
        compositeDisposable.clear();
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
    public void onReminderToggled(final boolean active, final RealmReminder reminder) {
        if (active != reminder.isActive()) {
            reminder.setActive(active);

            compositeDisposable.add(
                    reminderSource.updateReminder(reminder)
                            .subscribeOn(schedulerProvider.io())
                            .observeOn(schedulerProvider.ui())
                            .subscribeWith(new DisposableCompletableObserver() {
                                @Override
                                public void onError(Throwable e) {
                                    view.makeToast(R.string.error_database_write_failure);
                                }

                                @Override
                                public void onComplete() {
                                    if (active) {
                                        view.makeToast(R.string.msg_alarm_activated);
                                    } else {
                                        view.makeToast(R.string.msg_alarm_deactivated);
                                    }

                                }
                            }));
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

    @Override
    public void onSettingsIconClick() {
        view.startSettingsActivity();
    }

    @Override
    public void onReminderSwiped(final int position, final RealmReminder reminder) {
        compositeDisposable.add(
                reminderSource.deleteReminder(reminder.getReminderId())
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
    public void onReminderIconClick(RealmReminder reminder) {
        view.startReminderDetailActivity(reminder.getReminderId());
    }

    @Override
    public void onCreateReminderButtonClick(
            int currentNumberOfReminders,
            String defaultName,
            final String reminderId) {
        if (currentNumberOfReminders < 5) {
            final RealmReminder reminder = new RealmReminder(
                    reminderId,
                    12,
                    30,
                    defaultName,
                    false,
                    true,
                    false
                    );

            compositeDisposable.add(
                    reminderSource.createReminder(reminderId)
                            .subscribeOn(schedulerProvider.io())
                            .observeOn(schedulerProvider.ui())
                            .subscribeWith(new DisposableCompletableObserver() {
                                @Override
                                public void onComplete() {
                                    view.addNewReminderToListView(reminder);
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
