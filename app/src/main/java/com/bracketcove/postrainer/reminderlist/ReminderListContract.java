package com.bracketcove.postrainer.reminderlist;

import com.bracketcove.postrainer.baseinterfaces.BasePresenter;
import com.bracketcove.postrainer.baseinterfaces.BaseView;
import com.bracketcove.postrainer.data.reminder.RealmReminder;

import java.util.List;

/**
 * Created by Ryan on 06/03/2017.
 */

public interface ReminderListContract  {

    interface View extends BaseView<Presenter> {
        void setReminderListData(List<RealmReminder> reminderListData);

        void setNoReminderListDataFound();

        void addNewReminderToListView(RealmReminder reminder);

        void undoDeleteReminderAt(int index, RealmReminder reminder);

        void startReminderDetailActivity(String reminderId);

        void startSettingsActivity();


    }

    interface Presenter extends BasePresenter {
        void onReminderToggled(boolean active, RealmReminder reminder);

        void onSettingsIconClick();

        void onReminderSwiped(int index, RealmReminder reminder);

        void onReminderIconClick(RealmReminder reminder);

        void onCreateReminderButtonClick(int currentNumberOfReminders,String defaultName, String creationDate);
    }
}
