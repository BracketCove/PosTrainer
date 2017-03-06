package com.bracketcove.postrainer.alarm;


/**
 *
 * Created by Ryan on 05/03/2017.
 */

public class AlarmService implements AlarmSource {

    public AlarmService() {

    }

    public static AlarmService getInstance () {
        return new AlarmService();
    }




        /*
    private static final String BUNDLE_EXTRA = "BUNDLE_EXTRA";
    private static final String ITEM_EXTRA = "ITEM_EXTRA";
    private PowerManager.WakeLock wakeLock;
    private MediaPlayer mediaPlayer;

    private Vibrator vibe;

     implements MediaPlayer.OnPreparedListener

     Log.d("AR", "Alarm Fired");
    PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
    wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "My Wake Log");
        wakeLock.acquire();
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN |
            WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
            WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
                                  WindowManager.LayoutParams.FLAG_FULLSCREEN |
                                          WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                                          WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
    setContentView(R.layout.activity_alarm_receiver);

    Button stopAlarm = (Button) findViewById(R.id.stopAlarm);
        stopAlarm.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
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
    });

    Bundle extras = getIntent().getBundleExtra(BUNDLE_EXTRA);
    Reminder item = extras.getParcelable(ITEM_EXTRA);

        item.setActive(false);
    }
        vibrate();
    } else {
        vibrate();
        String toneURI = getAlarmUri().toString();
        playSound(this, Uri.parse(toneURI));
    }



    private void vibrate() {
        vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long[] vPatternOne = {0, 1000, 2000, 1000, 2000, 1000, 2000, 1000, 2000};
        vibe.vibrate(vPatternOne, -1);
    }

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
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mediaPlayer.start();
    }*/
}
