package com.bracketcove.postrainer.alarmreceiver;

import com.bracketcove.postrainer.R;
import com.bracketcove.postrainer.data.alarmdatabase.AlarmSource;
import com.bracketcove.postrainer.data.alarmservice.AlarmManager;
import com.bracketcove.postrainer.data.alarmservice.AlarmService;
import com.bracketcove.postrainer.data.alarmdatabase.AlarmDatabase;
import com.bracketcove.postrainer.data.viewmodel.Alarm;
import com.bracketcove.postrainer.usecase.DismissAlarm;
import com.bracketcove.postrainer.usecase.GetAlarm;
import com.bracketcove.postrainer.usecase.StartAlarm;
import com.bracketcove.postrainer.usecase.UpdateOrCreateAlarm;
import com.bracketcove.postrainer.util.BaseSchedulerProvider;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * Created by Ryan on 05/03/2017.
 */

public class AlarmReceiverPresenter implements AlarmReceiverContract.Presenter {

    private final DismissAlarm dismissAlarm;
    private final StartAlarm startAlarm;
    private final GetAlarm getAlarm;
    private final UpdateOrCreateAlarm updateOrCreateAlarm;

    private final AlarmReceiverContract.View view;
    private final BaseSchedulerProvider schedulerProvider;
    private final CompositeDisposable compositeDisposable;

    @Inject
    public AlarmReceiverPresenter(AlarmReceiverContract.View view,
                                  AlarmSource alarmSource,
                                  AlarmManager alarmManager,
                                  BaseSchedulerProvider schedulerProvider) {
        this.getAlarm = new GetAlarm(alarmSource);
        this.dismissAlarm = new DismissAlarm(alarmManager);
        this.startAlarm = new StartAlarm(alarmManager);
        this.updateOrCreateAlarm = new UpdateOrCreateAlarm(alarmSource);

        this.view = view;
        this.schedulerProvider = schedulerProvider;
        this.compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void start() {
        getAlarmFromDatabase();
    }

    @Override
    public void stop() {
        compositeDisposable.clear();
    }

    /**
     * Query the Alarm Database for a Alarm which matches the given reminderId passed
     * in from the Activity's extras.
     */
    private void getAlarmFromDatabase() {
        Alarm alarm = view.getViewModel();

        //TODO get via ID since getting ViewModel doesn't make sense here
        compositeDisposable.add(
                getAlarm.runUseCase(alarm.getAlarmId())
                        .subscribeOn(schedulerProvider.io())
                        .observeOn(schedulerProvider.ui())
                        .subscribeWith(new DisposableSubscriber<Alarm>() {
                            @Override
                            public void onNext(Alarm alarm) {
                                checkAlarmState(alarm);
                            }

                            @Override
                            public void onError(Throwable e) {
                                view.makeToast(R.string.error_database_connection_failure);
                                view.finishActivity();
                            }

                            @Override
                            public void onComplete() {

                            }
                        })
        );
    }

    /**
     * Checks whether the Alarm should be written as INACTIVE or left alone, based on
     * alarm.isRenewAutomatically
     *
     * @param alarm
     */
    private void checkAlarmState(final Alarm alarm) {
        if (alarm.isRenewAutomatically()) {
            startAlarm(alarm);
        } else {
            alarm.setActive(false);

            compositeDisposable.add(
                    updateOrCreateAlarm.runUseCase(alarm)
                            .subscribeOn(schedulerProvider.io())
                            .observeOn(schedulerProvider.ui())
                            .subscribeWith(
                                    new DisposableCompletableObserver() {
                                        @Override
                                        public void onComplete() {
                                            startAlarm(alarm);
                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            view.makeToast(R.string.error_database_write_failure);
                                        }
                                    })
            );
        }
    }


    private void startAlarm(Alarm alarm) {
        compositeDisposable.add(
                startAlarm.runUseCase(alarm)
                        .subscribeOn(schedulerProvider.io())
                        .observeOn(schedulerProvider.ui())
                        .subscribeWith(new DisposableCompletableObserver() {
                            @Override
                            public void onComplete() {
                            }

                            @Override
                            public void onError(Throwable e) {
                                view.makeToast(R.string.error_starting_alarm);
                            }
                        })
        );


    }


    @Override
    public void onAlarmDismissClick() {
        //first, stop the media player and vibrator
        compositeDisposable.add(
                dismissAlarm.runUseCase()
                        .subscribeOn(schedulerProvider.io())
                        .observeOn(schedulerProvider.ui())
                        .subscribeWith(
                                new DisposableCompletableObserver() {
                                    @Override
                                    public void onComplete() {
                                        view.finishActivity();
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                    }
                                })
        );
    }
}
