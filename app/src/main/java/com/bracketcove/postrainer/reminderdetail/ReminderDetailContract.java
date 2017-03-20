package com.bracketcove.postrainer.reminderdetail;

import com.bracketcove.postrainer.BasePresenter;
import com.bracketcove.postrainer.BaseView;
import com.bracketcove.postrainer.data.reminder.Reminder;

/**
 * Created by Ryan on 06/03/2017.
 */

public interface ReminderDetailContract {
    interface View extends BaseView<Presenter> {
        void setReminderTitle(String title);

        void setVibrateOnly(boolean active);

        void setRenewAutomatically(boolean active);

        void setPickerTime(int hour, int minute);

        void setCurrentAlarmState(boolean active);

        String getReminderTitle();

        String getReminderId();

        boolean getVibrateOnly();

        boolean getRenewAutomatically();

        int getPickerHour();

        int getPickerMinute();

        boolean getCurrentAlarmState();

        void startReminderListActivity();

    }

    interface Presenter extends BasePresenter {
        void onBackIconPress();

        void onDoneIconPress();
    }
}
