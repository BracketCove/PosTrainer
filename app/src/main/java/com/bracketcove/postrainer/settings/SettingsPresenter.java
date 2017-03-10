package com.bracketcove.postrainer.settings;

import com.bracketcove.postrainer.util.BaseSchedulerProvider;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Ryan on 05/03/2017.
 */

public class SettingsPresenter implements SettingsContract.Presenter {
    private SettingsContract.View view;
    private CompositeDisposable disposableSubscriptions;
    private BaseSchedulerProvider baseSchedulerProvider;

    public SettingsPresenter(SettingsContract.View view,
                             BaseSchedulerProvider baseSchedulerProvider) {
        this.view = view;
        this.baseSchedulerProvider = baseSchedulerProvider;
        this.disposableSubscriptions = new CompositeDisposable();
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public void onContactButtonClick() {

    }
}
