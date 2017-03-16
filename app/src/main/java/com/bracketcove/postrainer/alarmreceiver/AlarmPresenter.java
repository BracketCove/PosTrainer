package com.bracketcove.postrainer.alarmreceiver;

import android.util.Log;

import com.bracketcove.postrainer.data.reminder.ReminderSource;
import com.bracketcove.postrainer.reminderdetail.ReminderDetailContract;
import com.bracketcove.postrainer.util.BaseScheduler;

import java.util.Calendar;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Ryan on 05/03/2017.
 */

public class AlarmPresenter implements AlarmContract.Presenter {

    private final AlarmContract.View view;
    private final ReminderSource reminderSource;
    private final BaseScheduler schedulerProvider;
    private final CompositeDisposable compositeDisposable;

    @Inject
    public AlarmPresenter(AlarmContract.View view,
                          ReminderSource reminderSource,
                          BaseScheduler schedulerProvider) {
        this.view = view;
        this.reminderSource = reminderSource;
        this.schedulerProvider = schedulerProvider;
        this.compositeDisposable = new CompositeDisposable();

        //this may be better called elsewhere like subscribe(), or may need to be
        //injected via method injection. We'll see...
        this.view.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }
}
