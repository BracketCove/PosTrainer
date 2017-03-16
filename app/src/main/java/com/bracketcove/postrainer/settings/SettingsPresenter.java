package com.bracketcove.postrainer.settings;

import com.bracketcove.postrainer.data.reminder.ReminderSource;
import com.bracketcove.postrainer.reminderlist.ReminderListContract;
import com.bracketcove.postrainer.util.BaseScheduler;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Ryan on 05/03/2017.
 */

public class SettingsPresenter implements SettingsContract.Presenter {
    private final SettingsContract.View view;
    private final BaseScheduler schedulerProvider;
    private final CompositeDisposable compositeDisposable;

    @Inject
    public SettingsPresenter(SettingsContract.View view,
                             BaseScheduler schedulerProvider) {
        this.view = view;
        this.schedulerProvider = schedulerProvider;
        this.compositeDisposable = new CompositeDisposable();
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
