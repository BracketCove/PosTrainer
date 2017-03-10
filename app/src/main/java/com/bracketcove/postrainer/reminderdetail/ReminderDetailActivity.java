package com.bracketcove.postrainer.reminderdetail;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bracketcove.postrainer.R;
import com.bracketcove.postrainer.reminderlist.ReminderListFragment;
import com.bracketcove.postrainer.util.ActivityUtils;

import static android.R.attr.tag;


public class ReminderDetailActivity extends AppCompatActivity {
    private static final String FRAG_REMINDER_DETAIL = "FRAG_REMINDER_DETAIL";

    private FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_detail);

        ReminderDetailFragment fragment =  (ReminderDetailFragment) manager.findFragmentByTag(FRAG_REMINDER_DETAIL);

        if (fragment == null){
            fragment = ReminderDetailFragment.newInstance();

        }

        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                fragment,
                R.id.cont_reminder_list_fragment,
                FRAG_REMINDER_DETAIL
                );
    }
}
