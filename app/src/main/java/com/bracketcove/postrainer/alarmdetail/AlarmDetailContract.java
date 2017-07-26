package com.bracketcove.postrainer.alarmdetail;

import com.bracketcove.postrainer.base.BasePresenter;
import com.bracketcove.postrainer.base.BaseView;
import com.bracketcove.postrainer.data.viewmodel.Alarm;

/**
 * Created by Ryan on 06/03/2017.
 */

public interface AlarmDetailContract {
    interface View extends BaseView {
        Alarm getViewModel();

        void setAlarmTitle(String title);

        void setVibrateOnly(boolean active);

        void setRenewAutomatically(boolean active);

        void setPickerTime(int hour, int minute);

        void setCurrentAlarmState(boolean active);

        String getAlarmId();

        void startAlarmListActivity();

    }

    interface Presenter extends BasePresenter {
        void onBackIconPress();

        void onDoneIconPress();
    }
}
