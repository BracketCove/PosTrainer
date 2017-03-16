package com.bracketcove.postrainer.reminderdetail;

import android.util.Log;


import com.bracketcove.postrainer.data.reminder.ReminderSource;
import com.bracketcove.postrainer.util.BaseScheduler;

import java.util.Calendar;

import javax.inject.Inject;

import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Ryan on 05/03/2017.
 */

public class ReminderDetailPresenter implements ReminderDetailContract.Presenter {

    private final ReminderDetailContract.View view;
    private final ReminderSource reminderSource;
    private final BaseScheduler schedulerProvider;
    private final CompositeDisposable compositeDisposable;

    @Inject
    public ReminderDetailPresenter(ReminderDetailContract.View view,
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

    @Override
    public void onBackIconPress() {
        view.startReminderListActivity();
    }

    @Override
    public void onDoneIconPress() {
    }

    /**
     * All of this crap is just to create a unique identifier which is used in Database and for
     * creating unique intents.
     *
     * @return a unique id, based on the current time. Doesn't need to be fancy or stupidly long
     * like Calendar.getTimeInMillis()
     */
    public int getDate() {
        Calendar calendar = Calendar.getInstance();
        String date = "" + calendar.get(Calendar.DAY_OF_YEAR);
        date += "" + calendar.get(Calendar.HOUR_OF_DAY);
        date += "" + calendar.get(Calendar.MINUTE);
        date += "" + calendar.get(Calendar.SECOND);
        Log.d("TAG", "Date works out to: " + date);
        return Integer.parseInt(date);
    }
}
