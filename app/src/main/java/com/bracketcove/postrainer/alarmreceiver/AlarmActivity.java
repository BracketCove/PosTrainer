package com.bracketcove.postrainer.alarmreceiver;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.os.Vibrator;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.bracketcove.postrainer.R;
import com.bracketcove.postrainer.data.WriteToDatabase;
import com.bracketcove.postrainer.data.reminder.ReminderDatabase;
import com.bracketcove.postrainer.data.reminder.Reminder;
import com.bracketcove.postrainer.reminderlist.ReminderListActivity;

import java.io.IOException;

/**
 * This Activity is fired when an Alarm goes off. Once it is active, it handles the Alarm based on
 * the Alarm's configuration (such as Vibrate Only, etc.). It also serves as the method for stopping
 * an Alarm which is going off.
 * Created by Ryan on 17/04/2016.
 */
public class AlarmActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener,
        AlarmController {

    private static final String ALARM_FRAGMENT = "ALARM_FRAGMENT";

    private static final String VIBRATE_ONLY = "VIBRATE_ONLY";
    private static final String BUNDLE_ITEM = "BUNDLE_ITEM";
    private static final boolean VIBRATE_ONLY_DEFAULT = false;

    private MediaPlayer mediaPlayer;
    private Vibrator vibe;
    PowerManager.WakeLock wakeLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWakeLock();

        //TODO: Delete if found no longer useful
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Setting Theme.NoActionBar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        setContentView(R.layout.activity_alarm_receiver);

        setUpAlarmFragment();

        Reminder item = getIntent().getParcelableExtra(BUNDLE_ITEM);

        //Check if Alarm needs to be renewed automatically
        if (!item.isRenewAutomatically()) {
            item.setActive(false);
        }

        //writeReminderToDatabase(item);

        //if Alarm has Vibrate only flag set, don't play any sound
        if (getIntent().getBooleanExtra(VIBRATE_ONLY, VIBRATE_ONLY_DEFAULT)) {
            vibrate();
        } else {
            vibrate();
            String toneURI = getAlarmUri().toString();
            playSound(this, Uri.parse(toneURI));
        }
    }

    private void setUpAlarmFragment() {
        FragmentManager manager = this.getSupportFragmentManager();

        AlarmFragment fragment = (AlarmFragment) manager.findFragmentByTag(ALARM_FRAGMENT);

        if (fragment == null) {
            fragment = AlarmFragment.newInstance();
        }

        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.cont_alarm_fragment, fragment, ALARM_FRAGMENT)
                .commit();
    }

    /**
     * Acquires the wakeLock: when you call acquire(), the CPU will remain on.
     * Be sure to call wakeLock.release() as soon as the alarm is dismissed.
     */
    private void getWakeLock() {
        wakeLock = ((PowerManager) getSystemService(Context.POWER_SERVICE))
                .newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Alarm");
        wakeLock.acquire();
    }



    private void writeReminderToDatabase(Reminder item) {
        WriteToDatabase writer = new WriteToDatabase();
        writer.setData(item);
        writer.execute(ReminderDatabase.getInstance(getApplicationContext()));
        writer.setWriteCompleteListener(new WriteToDatabase.OnWriteComplete() {
            @Override
            public void setWriteComplete(long result) {

            }
        });
    }

    private void vibrate() {
        vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long[] vPatternOne = {0, 1000, 2000, 1000, 2000, 1000, 2000, 1000, 2000};
        vibe.vibrate(vPatternOne, -1);
    }

    /**
     * Retrieves a Uri to a Ringtone.
     * @return
     */
    //TODO: add some custom
    private Uri getAlarmUri() {
        Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alert == null) {
            alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            if (alert == null) {
                alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            }
        }
        return alert;
    }

    private void playSound(Context context, Uri alert) {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(context, alert);
            final AudioManager audioManager =
                    (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                new CountDownTimer(30000, 1000) {
                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {
                        if (mediaPlayer != null) {
                            mediaPlayer.stop();
                            mediaPlayer.release();
                            mediaPlayer = null;
                        }
                    }
                }.start();
                mediaPlayer.setOnPreparedListener(this);
                mediaPlayer.prepareAsync();
            }
        } catch (IOException e) {
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }

        wakeLock.release();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mediaPlayer.start();
    }


    @Override
    public void onDismissAlarmClick() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (vibe != null) {
            vibe.cancel();
        }
        finish();
        Intent i = new Intent(AlarmActivity.this, ReminderListActivity.class);
        startActivity(i);
    }

}
