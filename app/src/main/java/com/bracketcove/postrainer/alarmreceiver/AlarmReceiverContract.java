package com.bracketcove.postrainer.alarmreceiver;

import com.bracketcove.postrainer.BasePresenter;
import com.bracketcove.postrainer.BaseView;

/**
 * Created by Ryan on 05/03/2017.
 */

public interface AlarmReceiverContract {
    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {
        void onAlarmDismissClick();
    }
}
