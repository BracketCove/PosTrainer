package com.bracketcove.postrainer.alarmreceiver;

import com.bracketcove.postrainer.reminderdetail.ReminderDetailContract;

import dagger.Module;
import dagger.Provides;

/**
 * This is a feature level (think reminderdetail or reminderlist packages) Module. It satisfies the Presenter's
 * Dependency on the View Interface.
 * Created by Ryan on 10/03/2017.
 */
@Module
public class AlarmPresenterModule {
    //List what you would normally pass in as arguments to a Presenter's constructor
    private final AlarmContract.View view;

    public AlarmPresenterModule(AlarmContract.View view) {
        this.view = view;
    }

    @Provides
    AlarmContract.View provideReminderDetailView(){
        return view;
    }
}
