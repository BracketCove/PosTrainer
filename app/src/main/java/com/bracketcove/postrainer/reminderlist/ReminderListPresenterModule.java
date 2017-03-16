package com.bracketcove.postrainer.reminderlist;

import com.bracketcove.postrainer.reminderdetail.ReminderDetailContract;

import dagger.Module;
import dagger.Provides;

/**
 * This is a feature level (think reminderdetail or reminderlist packages) Module. It satisfies the Presenter's
 * Dependency on the View Interface.
 * Created by Ryan on 10/03/2017.
 */
@Module
public class ReminderListPresenterModule {
    //List what you would normally pass in as arguments to a Presenter's constructor
    private final ReminderListContract.View view;

    public ReminderListPresenterModule(ReminderListContract.View view) {
        this.view = view;

    }

    @Provides
    ReminderListContract.View provideReminderListView(){
        return view;
    }
}
