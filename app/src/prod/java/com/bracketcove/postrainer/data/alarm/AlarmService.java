package com.bracketcove.postrainer.data.alarm;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.PowerManager;
import android.os.Vibrator;

import com.bracketcove.postrainer.alarmreceiver.AlarmReceiverActivity;
import com.bracketcove.postrainer.data.reminder.Reminder;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

/**
 * This class is intended to decouple System Services from having to be run in
 * {@link AlarmReceiverActivity}. In theory, this will allow me to treat my alarmreceiver
 * component in a similar manner to which I treat the other ones (i.e. same class
 * structure/testing approach)
 * Created by Ryan on 17/03/2017.
 */

public class AlarmService implements AlarmSource {

    private final PowerManager.WakeLock wakeLock;
    private final AudioManager audioManager;
    private final Vibrator vibe;

    @Inject
    public AlarmService(PowerManager.WakeLock wakeLock,
                        AudioManager audioManager,
                        Vibrator vibe) {
        this.wakeLock = wakeLock;
        this.audioManager = audioManager;
        this.vibe = vibe;
    }

    @Override
    public void setReturnFail() {

    }

    @Override
    public void setReturnEmpty() {

    }

    @Override
    public Completable setAlarm(Reminder reminder) {
        return null;
    }

    @Override
    public Completable cancelAlarm(Reminder reminder) {
        return null;
    }

    @Override
    public Completable updateAlarm(Reminder reminder) {
        return null;
    }

}
