package com.bracketcove.postrainer.alarmdetail;


import com.bracketcove.postrainer.R;
import com.bracketcove.postrainer.data.alarmdatabase.AlarmDatabase;
import com.bracketcove.postrainer.data.alarmdatabase.AlarmSource;
import com.bracketcove.postrainer.data.viewmodel.Alarm;
import com.bracketcove.postrainer.usecase.GetAlarm;
import com.bracketcove.postrainer.usecase.UpdateOrCreateAlarm;
import com.bracketcove.postrainer.util.BaseSchedulerProvider;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * Created by Ryan on 05/03/2017.
 */

public class AlarmDetailPresenter implements AlarmDetailContract.Presenter {

    //Use Cases
    private final GetAlarm getAlarm;
    private final UpdateOrCreateAlarm updateOrCreateAlarm;


    private final AlarmDetailContract.View view;
    private final BaseSchedulerProvider schedulerProvider;
    private final CompositeDisposable compositeDisposable;

    //Constructor Injection: What is a use case for Constructor Injection?
    @Inject
    public AlarmDetailPresenter(AlarmDetailContract.View view,
                                AlarmSource alarmSource,
                                BaseSchedulerProvider schedulerProvider) {
        this.getAlarm = new GetAlarm(alarmSource);
        this.updateOrCreateAlarm = new UpdateOrCreateAlarm(alarmSource);

        this.view = view;
        this.schedulerProvider = schedulerProvider;
        this.compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void start() {
       getReminder();
    }

    @Override
    public void stop() {
        compositeDisposable.clear();
    }

    public void getReminder(){
        compositeDisposable.add(
                getAlarm.runUseCase(view.getAlarmId())
                        .subscribeOn(schedulerProvider.io())
                        .observeOn(schedulerProvider.ui())
                        .subscribeWith(
                                new DisposableSubscriber<Alarm>() {
                                    @Override
                                    public void onNext(Alarm alarm) {
                                        view.setAlarmTitle(alarm.getAlarmTitle());
                                        view.setVibrateOnly(alarm.isVibrateOnly());
                                        view.setRenewAutomatically(alarm.isRenewAutomatically());
                                        view.setPickerTime(alarm.getHourOfDay(), alarm.getMinute());
                                        view.setCurrentAlarmState(alarm.isActive());
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        view.makeToast(R.string.error_invalid_alarm_id);
                                        view.startAlarmListActivity();
                                    }

                                    @Override
                                    public void onComplete() {

                                    }
                                })
        );
    }

    @Override
    public void onBackIconPress() {
        view.startAlarmListActivity();
    }

    /**
     * Ensure that the RealmAlarm is updated in repository
     */
    @Override
    public void onDoneIconPress() {
        Alarm alarm = view.getViewModel();
        alarm.setAlarmId(view.getAlarmId());

        compositeDisposable.add(
                updateOrCreateAlarm.runUseCase(alarm)
                        .subscribeOn(schedulerProvider.io())
                        .observeOn(schedulerProvider.ui())
                        .subscribeWith(
                                new DisposableCompletableObserver() {
                                    @Override
                                    public void onComplete() {
                                        view.makeToast(R.string.message_database_write_successful);
                                        view.startAlarmListActivity();
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        view.makeToast(R.string.error_database_write_failure);
                                    }
                                })
        );
    }
}
