package com.bracketcove.postrainer.alarmreceiver;

import com.bracketcove.postrainer.base.BasePresenter;
import com.bracketcove.postrainer.base.BaseView;
import com.bracketcove.postrainer.data.viewmodel.Alarm;

/**
 * Created by Ryan on 05/03/2017.
 */

public interface AlarmReceiverContract {
    interface View extends BaseView {
            String getAlarmId();

            Alarm getViewModel();

            void finishActivity();
    }

    interface Presenter extends BasePresenter {
        void onAlarmDismissClick();

    }
}
