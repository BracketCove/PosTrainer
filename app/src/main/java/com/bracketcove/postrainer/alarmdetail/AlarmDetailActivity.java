package com.bracketcove.postrainer.alarmdetail;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bracketcove.postrainer.R;
import com.bracketcove.postrainer.alarmlist.AlarmListActivity;
import com.bracketcove.postrainer.util.ActivityUtils;

/**
 * I kind of treat Activites, like Containers:
 * - A Container is both an Entry Point, and kind of a Manager of the things within it.
 */
public class AlarmDetailActivity extends AppCompatActivity {
    private static final String FRAG_ALARM_DETAIL = "FRAG_ALARM_DETAIL";
    private static final String ALARM_TO_BE_EDITED = "ALARM_TO_BE_EDITED";

    private FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_detail);

        String reminderId = getIntent().getStringExtra(ALARM_TO_BE_EDITED);

        if (reminderId == null){
            startActivity(new Intent(this, AlarmListActivity.class));
        }

        manager = getSupportFragmentManager();

        AlarmDetailFragment fragment =  (AlarmDetailFragment) manager.findFragmentByTag(FRAG_ALARM_DETAIL);

        if (fragment == null){
            fragment = AlarmDetailFragment.newInstance(reminderId);
        }

        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                fragment,
                R.id.cont_alarm_detail_fragment,
                FRAG_ALARM_DETAIL
        );
    }
}
