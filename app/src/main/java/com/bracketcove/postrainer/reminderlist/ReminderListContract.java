package com.bracketcove.postrainer.reminderlist;

import com.bracketcove.postrainer.BasePresenter;
import com.bracketcove.postrainer.BaseView;
import com.bracketcove.postrainer.data.reminder.Reminder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ryan on 06/03/2017.
 */

public interface ReminderListContract  {

    interface View extends BaseView<Presenter> {
        void setReminderListData(List<Reminder> reminderListData);

        void setNoReminderListDataFound();
    }

    interface Presenter extends BasePresenter {
        void onAlarmToggled(boolean active);

        void onAlarmIconClicked();

        void onAlarmWidgetSwiped(int swipedItemPosition);
    }
}
