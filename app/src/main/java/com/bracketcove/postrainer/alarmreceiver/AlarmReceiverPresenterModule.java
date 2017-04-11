package com.bracketcove.postrainer.alarmreceiver;

import dagger.Module;
import dagger.Provides;

/**
 * This is a feature level (think reminderdetail or reminderlist packages) Module. It satisfies the Presenter's Dependency on the View Interface.
 * Created by Ryan on 10/03/2017.
 */
@Module
public class AlarmReceiverPresenterModule {
    //List what you would normally pass in as arguments to a Presenter's constructor
    private final AlarmReceiverContract.View view;

    public AlarmReceiverPresenterModule(AlarmReceiverContract.View view) {
        this.view = view;
    }

    @Provides
    AlarmReceiverContract.View provideAlarmReceiverView(){
        return view;
    }
}
