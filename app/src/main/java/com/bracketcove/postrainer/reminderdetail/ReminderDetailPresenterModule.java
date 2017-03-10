package com.bracketcove.postrainer.reminderdetail;

import com.bracketcove.postrainer.reminderservice.ReminderSource;
import com.bracketcove.postrainer.util.BaseScheduler;
import com.bracketcove.postrainer.util.FragmentScoped;

import dagger.Module;
import dagger.Provides;

/**
 * This class describes the Dependencies of a Presenter.
 * Created by Ryan on 10/03/2017.
 */
@FragmentScoped
@Module
public class ReminderDetailPresenterModule {
    //List what you would normally pass in as arguments to a Presenter's constructor
    private final ReminderDetailContract.View view;


    private final ReminderSource reminderSource;

    private final BaseScheduler baseScheduler;

    //Same as what Presenter's Constructor would be, except it's for the Module now.
    public ReminderDetailPresenterModule(ReminderDetailContract.View view,
                                         ReminderSource reminderSource,
                                         BaseScheduler baseScheduler) {
        this.view = view;
        this.reminderSource = reminderSource;
        this.baseScheduler = baseScheduler;
    }

    @Provides
    ReminderDetailContract.View provideReminderDetailView(){
        return view;
    }

    @Provides
    ReminderSource provideReminderSource(){
        return reminderSource;
    }

    @Provides
    BaseScheduler provideScheduler(){
        return baseScheduler;
    }
}
