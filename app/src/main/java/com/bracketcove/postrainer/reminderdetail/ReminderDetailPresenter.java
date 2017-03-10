package com.bracketcove.postrainer.reminderdetail;

import android.util.Log;

import com.bracketcove.postrainer.reminderservice.ReminderSource;
import com.bracketcove.postrainer.util.BaseSchedulerProvider;

import java.util.Calendar;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Ryan on 05/03/2017.
 */

public class ReminderDetailPresenter implements ReminderDetailContract.Presenter {

    private ReminderDetailContract.View view;
    private ReminderSource reminderSource;
    private BaseSchedulerProvider schedulerProvider;
    private CompositeDisposable disposableSubscriptions;

    public ReminderDetailPresenter(ReminderDetailContract.View view,
                                   ReminderSource reminderSource,
                                   BaseSchedulerProvider schedulerProvider) {
        this.view = view;
        this.reminderSource = reminderSource;
        this.schedulerProvider = schedulerProvider;
        this.disposableSubscriptions = new CompositeDisposable();
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public void onBackIconPress() {

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
        String date  = "" + calendar.get(Calendar.DAY_OF_YEAR);
        date += "" + calendar.get(Calendar.HOUR_OF_DAY);
        date += "" + calendar.get(Calendar.MINUTE);
        date += "" + calendar.get(Calendar.SECOND);
        Log.d("TAG", "Date works out to: " + date);
        return Integer.parseInt(date);
    }
}
