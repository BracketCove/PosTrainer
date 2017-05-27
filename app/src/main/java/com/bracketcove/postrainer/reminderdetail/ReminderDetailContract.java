package com.bracketcove.postrainer.reminderdetail;

import com.bracketcove.postrainer.base.BasePresenter;
import com.bracketcove.postrainer.base.BaseView;
import com.bracketcove.postrainer.data.viewmodel.Reminder;

/**
 * Created by Ryan on 06/03/2017.
 */

public interface ReminderDetailContract {
    interface View extends BaseView<Presenter> {
        Reminder getViewModel();

        void setReminderTitle(String title);

        void setVibrateOnly(boolean active);

        void setRenewAutomatically(boolean active);

        void setPickerTime(int hour, int minute);

        void setCurrentAlarmState(boolean active);

        String getReminderId();

        void startReminderListActivity();

    }

    interface Presenter extends BasePresenter {
        void onBackIconPress();

        void onDoneIconPress();
    }
}
