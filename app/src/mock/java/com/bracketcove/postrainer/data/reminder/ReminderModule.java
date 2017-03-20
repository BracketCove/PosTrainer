package com.bracketcove.postrainer.data.reminder;

import com.bracketcove.postrainer.schedulers.SchedulerProvider;
import com.bracketcove.postrainer.util.BaseSchedulerProvider;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Ryan on 16/03/2017.
 */

@Module
public class ReminderModule {

    // Constructor needs one parameter to instantiate.  
    public ReminderModule() {
    }

    @Singleton
    @Provides
    ReminderSource provideReminderSource(){
        return new FakeReminderRepository();
    }

    @Provides
    @Singleton
    BaseSchedulerProvider provideScheduler(){
        return SchedulerProvider.getInstance();
    }
}
