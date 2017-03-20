package com.bracketcove.postrainer.reminderdetail;

import dagger.Module;
import dagger.Provides;

/**
 * This is a feature level (think reminderdetail or reminderlist packages) Module. It satisfies the Presenter's
 * Dependency on the View Interface.
 * Created by Ryan on 10/03/2017.
 */
@Module
public class ReminderDetailPresenterModule {
    //List what you would normally pass in as arguments to a Presenter's constructor
    private final ReminderDetailContract.View view;

    public ReminderDetailPresenterModule(ReminderDetailContract.View view) {
        this.view = view;
    }

    @Provides
    ReminderDetailContract.View provideReminderDetailView(){
        return view;
    }
}
