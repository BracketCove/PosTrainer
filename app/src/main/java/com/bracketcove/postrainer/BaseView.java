package com.bracketcove.postrainer;

import android.support.annotation.StringRes;

/**
 * Created by Ryan on 05/03/2017.
 */


public interface BaseView<T> {

    void setPresenter(T presenter);

    void makeToast(String message);

}