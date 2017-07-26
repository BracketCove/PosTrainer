package com.bracketcove.postrainer.alarmdetail;

import dagger.Module;
import dagger.Provides;

/**
 * This is a feature level Module. It satisfies the Presenter's
 * Dependency on the View Interface.
 * Created by Ryan on 10/03/2017.
 */
@Module
public class AlarmDetailPresenterModule {
    //List what you would normally pass in as arguments to a Presenter's constructor
    private final AlarmDetailContract.View view;

    public AlarmDetailPresenterModule(AlarmDetailContract.View view) {
        this.view = view;
    }

    @Provides
    AlarmDetailContract.View provideAlarmDetailView(){
        return view;
    }
}
