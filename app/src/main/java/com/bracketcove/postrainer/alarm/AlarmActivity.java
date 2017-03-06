package com.bracketcove.postrainer.alarm;

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
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.bracketcove.postrainer.R;
import com.bracketcove.postrainer.database.WriteToDatabase;
import com.bracketcove.postrainer.database.AlarmDatabase;
import com.bracketcove.postrainer.database.Reminder;
import com.bracketcove.postrainer.reminderlist.ReminderListActivity;

import java.io.IOException;

/**
 * Created by Ryan on 17/04/2016.
 */
public class AlarmActivity extends AppCompatActivity  {

    private FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }




}
