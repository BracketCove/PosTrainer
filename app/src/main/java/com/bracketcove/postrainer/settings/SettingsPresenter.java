package com.bracketcove.postrainer.settings;

import javax.inject.Inject;

/**
 * Created by Ryan on 05/03/2017.
 */

public class SettingsPresenter implements SettingsContract.Presenter {
    private final SettingsContract.View view;

    @Inject
    public SettingsPresenter(SettingsContract.View view) {
        this.view = view;
    }

    @Inject
    void setPresenter() {
        view.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {

    }

    @Override
    public void onContactButtonClick() {

    }
}
