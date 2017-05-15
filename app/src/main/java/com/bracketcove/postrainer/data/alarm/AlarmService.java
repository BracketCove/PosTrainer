package com.bracketcove.postrainer.data.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.os.Vibrator;

import com.bracketcove.postrainer.alarmreceiver.AlarmReceiverActivity;
import com.bracketcove.postrainer.data.viewmodel.Reminder;

import java.util.Calendar;

import io.reactivex.Completable;

import static android.content.Context.POWER_SERVICE;

public class AlarmService implements AlarmSource {
    private static final String REMINDER_ID = "REMINDER_ID";

    private final PowerManager.WakeLock wakeLock;
    private MediaPlayer mediaPlayer;
    private final AudioManager audioManager;
    private final Vibrator vibe;
    private final AlarmManager alarmManager;
    private Context context;

    public AlarmService(Context applicationContext) {
        this.context = applicationContext;

        this.wakeLock = ((PowerManager) applicationContext
                .getSystemService(POWER_SERVICE))
                .newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Alarm");

        audioManager = ((AudioManager) applicationContext.getSystemService(Context.AUDIO_SERVICE));

        mediaPlayer = new MediaPlayer();

        vibe = ((Vibrator) applicationContext.getSystemService(Context.VIBRATOR_SERVICE));

        alarmManager = (AlarmManager) applicationContext.getSystemService(Context.ALARM_SERVICE);
    }


    @Override
    public Completable setAlarm(Reminder reminder) {
        Calendar alarm = Calendar.getInstance();
        alarm.setTimeInMillis(System.currentTimeMillis());
        alarm.set(Calendar.HOUR_OF_DAY, reminder.getHourOfDay());
        alarm.set(Calendar.MINUTE, reminder.getMinute());

        checkAlarm(alarm);

        Intent intent = new Intent(context, AlarmReceiverActivity.class);
        intent.putExtra(REMINDER_ID, reminder.getReminderId());
        PendingIntent alarmIntent = PendingIntent.getActivity(
                context,
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

        Intent intent = new Intent(context, AlarmReceiverActivity.class);

        PendingIntent alarmIntent = PendingIntent.getActivity(context,
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
        mediaPlayer.setDataSource(getAlarmUri());

        if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
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

            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });

            mediaPlayer.prepare();
        }
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
