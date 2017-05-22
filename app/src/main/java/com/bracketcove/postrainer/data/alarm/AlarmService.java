package com.bracketcove.postrainer.data.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.os.Vibrator;

import com.bracketcove.postrainer.alarmreceiver.AlarmReceiverActivity;
import com.bracketcove.postrainer.data.viewmodel.Reminder;

import java.util.Calendar;

import javax.inject.Inject;

import io.reactivex.Completable;

import static android.content.Context.POWER_SERVICE;

public class AlarmService implements AlarmSource {
    private static final String REMINDER_ID = "REMINDER_ID";

    private final PowerManager.WakeLock wakeLock;
    private MediaPlayer mediaPlayer;
    private final AudioManager audioManager;
    private final Vibrator vibe;
    private final AlarmManager alarmManager;
    private final Context applicationContext;

    /**
     * One unfortunate consequence of AlarmManager is that it requires Context in order to create
     * Intents. Ideally, I wouldn't be passing in any kind of Context here, but the Framework makes
     * this a necessity (until a better solution becomes apparent to me). I'll be monitoring
     * this Context object for Memory Leaks, as this is a possible scenario for such things to occur,
     * as I'm passing in ApplicationContext.
     * @param wakeLock
     * @param mediaPlayer
     * @param audioManager
     * @param vibe
     * @param alarmManager
     * @param applicationContext
     */
    @Inject
    public AlarmService(PowerManager.WakeLock wakeLock,
                        MediaPlayer mediaPlayer,
                        AudioManager audioManager,
                        Vibrator vibe,
                        AlarmManager alarmManager,
                        Context applicationContext) {

        this.wakeLock = wakeLock;
        this.mediaPlayer = mediaPlayer;
        this.audioManager = audioManager;
        this.vibe = vibe;
        this.alarmManager = alarmManager;
        this.applicationContext = applicationContext;
    }


    @Override
    public Completable setAlarm(Reminder reminder) {
        Calendar alarm = Calendar.getInstance();
        alarm.setTimeInMillis(System.currentTimeMillis());
        alarm.set(Calendar.HOUR_OF_DAY, reminder.getHourOfDay());
        alarm.set(Calendar.MINUTE, reminder.getMinute());

        checkAlarm(alarm);

        Intent intent = new Intent(applicationContext, AlarmReceiverActivity.class);
        intent.putExtra(REMINDER_ID, reminder.getReminderId());
        PendingIntent alarmIntent = PendingIntent.getActivity(
                applicationContext,
                Integer.parseInt(reminder.getReminderId()),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        if (reminder.isRenewAutomatically()) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, alarm.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, alarmIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, alarm.getTimeInMillis(),
                    alarmIntent);
        }

        return Completable.complete();
    }

    private void checkAlarm(Calendar alarm) {
        Calendar now = Calendar.getInstance();
        if (alarm.before(now)) {
            long alarmForFollowingDay = alarm.getTimeInMillis() + 86400000L;
            alarm.setTimeInMillis(alarmForFollowingDay);
        }
    }

    @Override
    public Completable cancelAlarm(Reminder reminder) {

        Intent intent = new Intent(applicationContext, AlarmReceiverActivity.class);

        PendingIntent alarmIntent = PendingIntent.getActivity(applicationContext,
                Integer.parseInt(reminder.getReminderId()),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.cancel(alarmIntent);

        return Completable.complete();
    }

    @Override
    public Completable stopMediaPlayerAndVibrator() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (vibe != null) {
            vibe.cancel();
        }

        return Completable.complete();
    }

    /**
     * Starts an Alarm with the Requisite Parameters:
     */
    @Override
    public Completable startAlarm(Reminder reminder) {
        wakeLock.acquire();

        if (reminder.isVibrateOnly()) {
            vibratePhone();
        } else {
            vibratePhone();
            try {
                playAlarmSound();
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }

        return Completable.complete();
    }


    @Override
    public Completable releaseWakeLock() {
        /*
        a recent bug appears to cause crashes by calling release on wakeLocks that don't
        pass WakeLock.isHeld(). important to note.
         */
        if (wakeLock != null && wakeLock.isHeld()) {
            wakeLock.release();
        }

        return Completable.complete();
    }

    private void playAlarmSound() throws java.io.IOException {
        new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                }
            }
        };

        mediaPlayer.start();
    }

    private void vibratePhone() {
        long[] vPatternOne = {0, 1000, 2000, 1000, 2000, 1000, 2000, 1000, 2000};
        vibe.vibrate(vPatternOne, -1);
    }

    private String getAlarmUri() {
        String alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM).toString();
        if (alert == null) {
            alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION).toString();
            if (alert == null) {
                alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE).toString();
            }
        }
        return alert;
    }
}
