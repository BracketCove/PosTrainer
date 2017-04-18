package com.bracketcove.postrainer.dependencyinjection;

import android.app.Application;
import android.content.Context;
import android.media.AudioManager;
import android.os.PowerManager;
import android.os.Vibrator;

import com.bracketcove.postrainer.data.alarm.AlarmService;
import com.bracketcove.postrainer.data.alarm.AlarmSource;
import com.bracketcove.postrainer.data.reminder.ReminderService;
import com.bracketcove.postrainer.data.reminder.ReminderSource;
import com.bracketcove.postrainer.scheduler.SchedulerProvider;
import com.bracketcove.postrainer.util.BaseSchedulerProvider;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static android.content.Context.POWER_SERVICE;

/**
 * This all made more sense after watching
 * <a href="https://www.youtube.com/watch?v=plK0zyRLIP8&t=92s">This talk.</a>
 * <p>
 * This is a Module. It provides dependencies via @Provides annotated methods below.
 * Module's are designed to be partitioned in a way that makes sense.
 * Created by Ryan on 10/03/2017.
 */
@Module
public final class ApplicationModule {
    private final Context applicationContext;

    public ApplicationModule(Context application) {
        this.applicationContext = application;
    }

    @Provides
    @Singleton
    Context provideContext() {
        return applicationContext;
    }

    @Singleton
    @Provides
    PowerManager.WakeLock provideWakeLock() {
        return ((PowerManager) applicationContext
                .getSystemService(POWER_SERVICE))
                .newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Alarm");
    }

    @Singleton
    @Provides
    AudioManager provideAudioManager() {
        return ((AudioManager) applicationContext.getSystemService(Context.AUDIO_SERVICE));
    }

    @Singleton
    @Provides
    Vibrator provideVibrator() {
        return ((Vibrator) applicationContext.getSystemService(Context.VIBRATOR_SERVICE));
    }

}
