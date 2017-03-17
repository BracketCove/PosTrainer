package com.bracketcove.postrainer.di;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.bracketcove.postrainer.data.reminder.ReminderRepository;
import com.bracketcove.postrainer.data.reminder.ReminderSource;
import com.bracketcove.postrainer.util.BaseScheduler;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
        return new ReminderRepository();
    }

    @Provides
    @Singleton
    public BaseScheduler provideScheduler(){
        return new com.bracketcove.postrainer.schedulers.Scheduler();
    }
}
