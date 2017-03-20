package com.bracketcove.postrainer.data.alarm;

import android.media.MediaPlayer;
import android.os.PowerManager;
import android.os.Vibrator;

import com.bracketcove.postrainer.alarmreceiver.AlarmReceiverActivity;

import javax.inject.Inject;

/**
 * This class is intended to decouple System Services from having to be run in
 * {@link AlarmReceiverActivity}. In theory, this will allow me to treat my alarmreceiver
 * component in a similar manner to which I treat the other ones (i.e. same class
 * structure/testing approach)
 * Created by Ryan on 17/03/2017.
 */

public class AlarmService {

    private final PowerManager.WakeLock wakeLock;
    private final MediaPlayer mediaPlayer;
    private final Vibrator vibe;

    @Inject
    public AlarmService(PowerManager.WakeLock wakeLock,
                        MediaPlayer mediaPlayer,
                        Vibrator vibe) {
        this.wakeLock = wakeLock;
        this.mediaPlayer = mediaPlayer;
        this.vibe = vibe;
    }
}
