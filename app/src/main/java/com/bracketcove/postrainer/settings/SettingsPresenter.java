package com.bracketcove.postrainer.settings;

import com.bracketcove.postrainer.util.BaseScheduler;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Ryan on 05/03/2017.
 */

public class SettingsPresenter implements SettingsContract.Presenter {
    private SettingsContract.View view;
    private CompositeDisposable disposableSubscriptions;
    private BaseScheduler baseScheduler;

    public SettingsPresenter(SettingsContract.View view,
                             BaseScheduler baseScheduler) {
        this.view = view;
        this.baseScheduler = baseScheduler;
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
