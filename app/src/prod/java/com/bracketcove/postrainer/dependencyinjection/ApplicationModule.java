package com.bracketcove.postrainer.dependencyinjection;

import android.app.Application;
import android.content.Context;
import android.media.AudioManager;
import android.os.PowerManager;
import android.os.Vibrator;


import com.bracketcove.postrainer.data.alarm.AlarmService;
import com.bracketcove.postrainer.data.alarm.AlarmSource;
import com.bracketcove.postrainer.data.reminder.ReminderRepository;
import com.bracketcove.postrainer.data.reminder.ReminderSource;
import com.bracketcove.postrainer.schedulers.SchedulerProvider;
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
    //TODO is it safe that this is Application and not PostrainerApplication?
    private final Context applicationContext;

    public ApplicationModule(Application application) {
        this.applicationContext = application;
    }

    /**
     * A Module method which provides a dependency, should be annotated with (at)Provides,
     * like so. This is a "hook" which tells Dagger that there is a dependency that can be grabbed
     * from here. Also, the since we only want a Single Instance ever, we use the Singleton
     * annotation to tell dagger.
     *
     * @return
     */
    @Provides
    @Singleton
    Context provideApplication() {
        return applicationContext;
    }

    @Singleton
    @Provides
    PowerManager.WakeLock provideWakeLock() {
        PowerManager powerManager = (PowerManager) applicationContext.getSystemService(POWER_SERVICE);
        return ((PowerManager) applicationContext
                .getSystemService(POWER_SERVICE))
                .newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Alarm");
    }

    @Singleton
    @Provides
    AudioManager provideAudioManager() {
        PowerManager powerManager = (PowerManager) applicationContext.getSystemService(POWER_SERVICE);
        return ((AudioManager) applicationContext.getSystemService(Context.AUDIO_SERVICE));
    }

    @Singleton
    @Provides
    Vibrator provideVibrator() {
        PowerManager powerManager = (PowerManager) applicationContext.getSystemService(POWER_SERVICE);
        return ((Vibrator) applicationContext.getSystemService(Context.VIBRATOR_SERVICE));
    }

    @Provides
    @Singleton
    BaseSchedulerProvider provideScheduler() {
        return SchedulerProvider.getInstance();
    }

    @Singleton
    @Provides
    ReminderSource provideReminderSource(){
        return new ReminderRepository();
    }

    @Singleton
    @Provides
    AlarmSource provideAlarmSource(){
        return new AlarmService(provideWakeLock(), provideAudioManager(), provideVibrator());
    }
}
