package com.bracketcove.postrainer.dependencyinjection.modules;

import android.content.Context;

import com.bracketcove.postrainer.data.alarm.AlarmService;
import com.bracketcove.postrainer.data.alarm.AlarmSource;
import com.bracketcove.postrainer.data.reminder.ReminderService;
import com.bracketcove.postrainer.data.reminder.ReminderSource;
import com.bracketcove.postrainer.data.viewmodel.Reminder;
import com.bracketcove.postrainer.util.BaseSchedulerProvider;
import com.bracketcove.postrainer.util.SchedulerProvider;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

/**
 * Created by Ryan on 12/04/2017.
 */
@Module
public class ReminderModule {

    public ReminderModule (Realm realm){
        this.reminderService = new ReminderService();
    }

    ReminderService reminderService;

    @Singleton
    @Provides
    ReminderService provideReminderService() {
        return reminderService;
    }

}
