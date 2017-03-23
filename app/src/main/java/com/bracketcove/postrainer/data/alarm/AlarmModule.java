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


}
