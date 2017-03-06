package com.bracketcove.postrainer;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import com.bracketcove.postrainer.reminderlist.ReminderListActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Simple test to ensure that when an Alarm is Activated or Disabled, the appropriate changes
 * are made to the label beside the switch.
 * Created by Ryan on 07/09/2016.
 */
@RunWith(AndroidJUnit4.class)
public class AlarmLabelTest {

    @Rule
    public ActivityTestRule<ReminderListActivity> ReminderActivityTestRule =
            new ActivityTestRule<ReminderListActivity>(ReminderListActivity.class);

    @Test
    public void toggleAlarmSwitch_changesLabelText() throws Exception {
        onView(withId(R.id.swi_alarm_activation)).perform(click());
    }
}
