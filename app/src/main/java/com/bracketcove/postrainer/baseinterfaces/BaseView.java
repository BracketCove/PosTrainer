package com.bracketcove.postrainer.baseinterfaces;

import android.support.annotation.StringRes;

/**
 * Created by Ryan on 05/03/2017.
 */


public interface BaseView<T> {

    /* Probably not necessary anymore due to DI
    void setPresenter(T presenter);
    */

    void makeToast(@StringRes int message);

}