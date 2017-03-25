package com.bracketcove.postrainer.data.alarm;

import android.media.AudioManager;
import android.os.PowerManager;
import android.os.Vibrator;

import com.bracketcove.postrainer.dependencyinjection.ApplicationModule;
import com.bracketcove.postrainer.util.BaseSchedulerProvider;

import javax.inject.Singleton;

import dagger.Component;

/**
 *
 * To be deleted most likely
 * Created by Ryan on 16/03/2017.
 */

@Singleton
@Component(modules = {ApplicationModule.class} )
public interface AlarmComponent {
    PowerManager.WakeLock wakeLock();
    AudioManager audioManager();
    Vibrator vibrator();
    BaseSchedulerProvider scheduler();
}
