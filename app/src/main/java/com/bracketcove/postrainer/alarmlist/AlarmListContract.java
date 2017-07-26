package com.bracketcove.postrainer.alarmlist;

import com.bracketcove.postrainer.base.BasePresenter;
import com.bracketcove.postrainer.base.BaseView;
import com.bracketcove.postrainer.data.viewmodel.Alarm;

import java.util.List;

/**
 * Created by Ryan on 06/03/2017.
 */

public interface AlarmListContract {
    interface View extends BaseView {
        void setAlarmListData(List<Alarm> alarmListData);

        void setNoAlarmListDataFound();

        void addNewAlarmToListView(Alarm alarm);

        void startAlarmDetailActivity(String alarmId);

        void startSettingsActivity();

        void showUndoSnackbar();

        void insertAlarmAt(int position, Alarm alarm);


    }

    interface Presenter extends BasePresenter {
        void onAlarmToggled(boolean active, Alarm alarm);

        void onSettingsIconClick();

        void onAlarmSwiped(int index, Alarm alarm);

        void onAlarmIconClick(Alarm alarm);

        void onCreateAlarmButtonClick(int currentNumberOfAlarms, Alarm alarm);

        void onUndoConfirmed();

        void onSnackbarTimeout();


    }
}
