package com.bracketcove.postrainer.data.alarm;

import android.content.Context;
import android.media.AudioManager;
import android.os.PowerManager;
import android.os.Vibrator;

import com.bracketcove.postrainer.schedulers.SchedulerProvider;
import com.bracketcove.postrainer.util.BaseSchedulerProvider;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static android.content.Context.POWER_SERVICE;

/**
 * Created by Ryan on 16/03/2017.
 */

@Module
public class AlarmModule {
    private Context context;


    public AlarmModule(Context context) {
        this.context = context;
    }

    @Singleton
    @Provides
    PowerManager.WakeLock provideWakeLock(){
        PowerManager powerManager = (PowerManager) context.getSystemService(POWER_SERVICE);
        return ((PowerManager) context
                .getSystemService(Context.POWER_SERVICE))
                .newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Alarm");
    }

    @Singleton
    @Provides
    AudioManager provideAudioManager(){
        PowerManager powerManager = (PowerManager) context.getSystemService(POWER_SERVICE);
        return ((AudioManager) context.getSystemService(Context.AUDIO_SERVICE));
    }

    @Singleton
    @Provides
    Vibrator provideVibrator(){
        PowerManager powerManager = (PowerManager) context.getSystemService(POWER_SERVICE);
        return ((Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE))
                ;
    }

    @Provides
    @Singleton
    BaseSchedulerProvider provideScheduler(){
        return SchedulerProvider.getInstance();
    }
}
