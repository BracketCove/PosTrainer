package com.bracketcove.postrainer.reminderlist;

import com.bracketcove.postrainer.database.AlarmSource;
import com.bracketcove.postrainer.util.BaseSchedulerProvider;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Ryan on 05/03/2017.
 */

public class ReminderListPresenter implements ReminderListContract.Presenter {

    private ReminderListContract.View view;
    private CompositeDisposable disposableSubscriptions;
    private AlarmSource alarmSource;
    private BaseSchedulerProvider schedulerProvider;


    public ReminderListPresenter(ReminderListContract.View view,
                                 AlarmSource alarmSource,
                                 BaseSchedulerProvider schedulerProvider) {
        this.view = view;
        this.alarmSource = alarmSource;
        this.schedulerProvider = schedulerProvider;

    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public void onAlarmToggled(boolean active) {

    }

    @Override
    public void onAlarmIconClicked() {

    }

    @Override
    public void onAlarmWidgetSwiped() {

    }
}
