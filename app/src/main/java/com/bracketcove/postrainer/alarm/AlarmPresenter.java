package com.bracketcove.postrainer.alarm;

import com.bracketcove.postrainer.util.BaseSchedulerProvider;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Ryan on 05/03/2017.
 */

public class AlarmPresenter implements AlarmContract.Presenter {
    private AlarmContract.View view;
    private AlarmSource alarmSource;
    private BaseSchedulerProvider schedulerProvider;
    private CompositeDisposable compositeDisposable;


    public AlarmPresenter(AlarmContract.View view,
                          AlarmSource alarmSource,
                          BaseSchedulerProvider schedulerProvider
                            ) {
        this.view = view;


    }


    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public void onStopAlarmButtonClick() {

    }
}
