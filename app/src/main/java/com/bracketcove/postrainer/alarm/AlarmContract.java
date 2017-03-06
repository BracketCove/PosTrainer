package com.bracketcove.postrainer.alarm;

import com.bracketcove.postrainer.BasePresenter;
import com.bracketcove.postrainer.BaseView;

/**
 * Created by Ryan on 05/03/2017.
 */

public interface AlarmContract {

    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {
        void onStopAlarmButtonClick();
    }
}
