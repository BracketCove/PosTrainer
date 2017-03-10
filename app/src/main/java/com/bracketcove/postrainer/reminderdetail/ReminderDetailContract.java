package com.bracketcove.postrainer.reminderdetail;

import com.bracketcove.postrainer.BasePresenter;
import com.bracketcove.postrainer.BaseView;

/**
 * Created by Ryan on 06/03/2017.
 */

public interface ReminderDetailContract {
    interface View extends BaseView<Presenter> {
        void setAlarmTitle(String title);

        void setVibrateOnly(boolean active);

        void setRenewAutomatically(boolean active);

        void setPickerTime(int hour, int minute);

        void startReminderListActivity();

        void showDatabaseErrorMessage();

        void showDatabaseUpdatedMessage();
    }

    interface Presenter extends BasePresenter {
        void onBackIconPress();

        void onDoneIconPress();
    }
}
