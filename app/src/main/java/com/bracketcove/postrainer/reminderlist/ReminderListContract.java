package com.bracketcove.postrainer.reminderlist;

import com.bracketcove.postrainer.BasePresenter;
import com.bracketcove.postrainer.BaseView;
import com.bracketcove.postrainer.database.Reminder;

import java.util.ArrayList;

/**
 * Created by Ryan on 06/03/2017.
 */

public interface ReminderListContract  {

    interface View extends BaseView<Presenter> {
        void setReminderListData(ArrayList<Reminder> reminderListData);

        void setNoReminderListDataFound();
    }

    interface Presenter extends BasePresenter {
        void onAlarmToggled(boolean active);

        void onAlarmIconClicked();

        void onAlarmWidgetSwiped();
    }
}
