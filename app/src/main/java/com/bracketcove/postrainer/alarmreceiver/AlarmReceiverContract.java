package com.bracketcove.postrainer.alarmreceiver;

import com.bracketcove.postrainer.base.BasePresenter;
import com.bracketcove.postrainer.base.BaseView;

/**
 * Created by Ryan on 05/03/2017.
 */

public interface AlarmReceiverContract {
    interface View extends BaseView<Presenter> {
            String getReminderId();

            void finishActivity();
    }

    interface Presenter extends BasePresenter {
        void onAlarmDismissClick();

    }
}
