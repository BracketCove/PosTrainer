package com.bracketcove.postrainer.alarmreceiver;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.bracketcove.postrainer.PostrainerApplication;
import com.bracketcove.postrainer.R;
import com.bracketcove.postrainer.util.ActivityUtils;

import javax.inject.Inject;

/**
 * This Activity is fired when an Alarm goes off. Once it is active, it handles the Alarm based on
 * the Alarm's configuration (such as Vibrate Only, etc.). It also serves as the method for stopping
 * an Alarm which is going off.
 * Created by Ryan on 17/04/2016.
 */
public class AlarmReceiverActivity extends AppCompatActivity {

    private static final String ALARM_FRAGMENT = "ALARM_FRAGMENT";
    private static final String BUNDLE_ALARM_DATA = "BUNDLE_ALARM_DATA";

    @Inject
    AlarmReceiverPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentManager manager = this.getSupportFragmentManager();

        AlarmReceiverFragment fragment = (AlarmReceiverFragment) manager.findFragmentByTag(ALARM_FRAGMENT);

        if (fragment == null) {
            fragment = AlarmReceiverFragment.newInstance();
        }

        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                fragment,
                R.id.cont_alarm_fragment,
                ALARM_FRAGMENT
        );

        DaggerAlarmReceiverComponent.builder()
                .alarmReceiverPresenterModule(new AlarmReceiverPresenterModule(fragment))
                .reminderComponent(
                        ((PostrainerApplication) getApplication())
                                .getReminderComponent()
                )
                .build().inject(this);
    }
}
