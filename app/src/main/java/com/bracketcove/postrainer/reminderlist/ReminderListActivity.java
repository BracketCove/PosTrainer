package com.bracketcove.postrainer.reminderlist;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.bracketcove.postrainer.R;

public class ReminderListActivity extends AppCompatActivity {
    private static final String FRAG_REMINDER_LIST = "FRAG_REMINDER_LIST";
    private static final String FRAG_MANAGE_REMINDER = "FRAG_MANAGE_REMINDER";

    private FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders);

        //Toolbar
       // toolbar = (Toolbar) findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);



        manager = getSupportFragmentManager();
    }

}
