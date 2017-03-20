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

        void addNewReminderToListView(Reminder reminder);

        void undoDeleteReminderAt(int index, Reminder reminder);

        void startReminderDetailActivity(String reminderId);

        void startSettingsActivity();


    }

    interface Presenter extends BasePresenter {
        void onReminderToggled(boolean active, Reminder reminder);

        void onSettingsIconClick();

        void onReminderSwiped(int index, Reminder reminder);

        void onReminderIconClick(Reminder reminder);

        void onCreateReminderButtonClick(int currentNumberOfReminders,String defaultName, String creationDate);
    }
}
