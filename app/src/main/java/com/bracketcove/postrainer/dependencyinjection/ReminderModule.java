package com.bracketcove.postrainer.dependencyinjection;

import android.content.Context;

import com.bracketcove.postrainer.data.alarm.AlarmService;
import com.bracketcove.postrainer.data.alarm.AlarmSource;
import com.bracketcove.postrainer.data.reminder.ReminderService;
import com.bracketcove.postrainer.data.reminder.ReminderSource;
import com.bracketcove.postrainer.scheduler.SchedulerProvider;
import com.bracketcove.postrainer.util.BaseSchedulerProvider;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Ryan on 12/04/2017.
 */
@Module
public class ReminderModule {
    @Singleton
    @Provides
    ReminderSource provideReminderSource() {
        return new ReminderService();
    }

    @Singleton
    @Provides
    AlarmSource provideAlarmSource(Context context) {
        return new AlarmService(context);
    }

    @Provides
    @Singleton
    BaseSchedulerProvider provideScheduler() {
        return new SchedulerProvider();
    }
}
