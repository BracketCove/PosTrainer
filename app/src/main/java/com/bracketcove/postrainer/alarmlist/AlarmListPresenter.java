package com.bracketcove.postrainer.alarmlist;

import com.bracketcove.postrainer.R;
import com.bracketcove.postrainer.data.alarmdatabase.AlarmSource;
import com.bracketcove.postrainer.data.alarmservice.AlarmManager;
import com.bracketcove.postrainer.data.alarmservice.AlarmService;
import com.bracketcove.postrainer.data.alarmdatabase.AlarmDatabase;
import com.bracketcove.postrainer.data.viewmodel.Alarm;
import com.bracketcove.postrainer.usecase.CancelAlarm;
import com.bracketcove.postrainer.usecase.DeleteAlarm;
import com.bracketcove.postrainer.usecase.GetAlarmList;
import com.bracketcove.postrainer.usecase.SetAlarm;
import com.bracketcove.postrainer.usecase.UpdateOrCreateAlarm;
import com.bracketcove.postrainer.util.BaseSchedulerProvider;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * Retrieves a List of any Reminders which are Present in AlarmDatabase (Realm Database), and
 * displays passes them to the View.
 * Created by Ryan on 05/03/2017.
 */

public class AlarmListPresenter implements AlarmListContract.Presenter {

    private final AlarmListContract.View view;
    private final BaseSchedulerProvider schedulerProvider;
    private final CompositeDisposable compositeDisposable;

    //Use Cases
    private final GetAlarmList getAlarmList;
    private final UpdateOrCreateAlarm updateOrCreateAlarm;
    private final DeleteAlarm deleteAlarm;
    private final SetAlarm setAlarm;
    private final CancelAlarm cancelAlarm;

    //Accidental RecyclerView Deletion (kind of a patchwork solution, not really happy with it
    // for some reason):
    private Alarm temporaryAlarm;
    private int temporaryAlarmPosition;

    @Inject
    public AlarmListPresenter(AlarmListContract.View view,
                              AlarmSource alarmSource,
                              AlarmManager alarmManager,
                              BaseSchedulerProvider schedulerProvider) {

        this.getAlarmList = new GetAlarmList(alarmSource);
        this.updateOrCreateAlarm = new UpdateOrCreateAlarm(alarmSource);
        this.deleteAlarm = new DeleteAlarm(alarmSource);
        this.setAlarm = new SetAlarm(alarmManager);
        this.cancelAlarm = new CancelAlarm(alarmManager);

        this.view = view;
        this.schedulerProvider = schedulerProvider;
        this.compositeDisposable = new CompositeDisposable();


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
     * Nothing : Display create RealmAlarm Prompt to User
     * error : Display database error
     */
    private void getReminders() {
        compositeDisposable.add(
                getAlarmList.runUseCase()
                        .subscribeOn(schedulerProvider.io())
                        .observeOn(schedulerProvider.ui())
                        .subscribeWith(
                                new DisposableSubscriber<List<Alarm>>() {
                                    @Override
                                    public void onNext(List<Alarm> alarms) {
                                        view.setAlarmListData(alarms);
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        view.makeToast(R.string.error_database_connection_failure);

                                    }

                                    @Override
                                    public void onComplete() {
                                        view.setNoAlarmListDataFound();
                                    }
                                }
                        )
        );


    }


    /**
     * Compares a desired alarm to the state of the current alarm.
     * - If states matches, we shouldn't need to update the Repository (I think so anyway).
     * - If state doesn't match, animate the toggle in the view, and update the Repository to
     * desired state
     *
     * @param active user's desired state of the alarm (probably)
     * @param alarm  current state of alarm in View. Assumed to be current with Repository
     */
    @Override
    public void onAlarmToggled(final boolean active, final Alarm alarm) {
        if (active != alarm.isActive()) {
            alarm.setActive(active);

            compositeDisposable.add(
                    updateOrCreateAlarm.runUseCase(alarm)
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
                                                onAlarmSet(alarm);
                                            } else {
                                                onAlarmCancelled(alarm);
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

    private void onAlarmSet(Alarm alarm) {
        compositeDisposable.add(
                setAlarm.runUseCase(alarm)
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

    private void onAlarmCancelled(Alarm alarm) {
        compositeDisposable.add(
                cancelAlarm.runUseCase(alarm)
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
    public void onAlarmSwiped(final int position, final Alarm alarm) {
        alarm.setActive(false);
        temporaryAlarm = alarm;
        temporaryAlarmPosition = position;

        compositeDisposable.add(
                deleteAlarm.runUseCase(alarm)
                        .subscribeOn(schedulerProvider.io())
                        .observeOn(schedulerProvider.ui())
                        .subscribeWith(new DisposableCompletableObserver() {
                            @Override
                            public void onComplete() {
                                view.showUndoSnackbar();
                                onAlarmCancelled(alarm);
                            }

                            @Override
                            public void onError(Throwable e) {
                                view.makeToast(R.string.error_database_connection_failure);
                                view.insertAlarmAt(position, alarm);
                            }
                        })
        );
    }

    @Override
    public void onAlarmIconClick(Alarm alarm) {
        view.startAlarmDetailActivity(alarm.getAlarmId());
    }

    @Override
    public void onCreateAlarmButtonClick(int currentNumberOfAlarms,
                                         Alarm alarm) {

        //only allow up to 5 Reminders at a time
        if (currentNumberOfAlarms < 5) {
            compositeDisposable.add(
                    updateOrCreateAlarm.runUseCase(alarm)
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
            view.makeToast(R.string.msg_alarm_limit_reached);
        }
    }

    /**
     * Add the Alarm back in to the Database and View on Undo
     */
    @Override
    public void onUndoConfirmed() {
        //TODO Realm Implementation:
        /*
        My Realm Impl. isn't yet set up to insert the Alarm back into a specific position within the
        database itself. This apparently requires adding a Field for position to the Models.
         */
        if (temporaryAlarm != null) {
            //ensure View/Data consistency
            compositeDisposable.add(
                    updateOrCreateAlarm.runUseCase(temporaryAlarm)
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

                                            view.insertAlarmAt(temporaryAlarmPosition, temporaryAlarm);
                                            temporaryAlarm = null;
                                            temporaryAlarmPosition = 0;
                                        }
                                    }
                            )
            );




        } else {
            view.makeToast(R.string.error_unable_to_retrieve_alarm);
        }

    }

    /**
     * Once the snackbar disappears, the user has lost the chance to undo their delete, and we need
     * not hold on to the temporary fields
     */
    @Override
    public void onSnackbarTimeout() {
        temporaryAlarm = null;
        temporaryAlarmPosition = 0;
    }
}
