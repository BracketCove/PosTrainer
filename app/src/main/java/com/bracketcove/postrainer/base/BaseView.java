package com.bracketcove.postrainer.base;

import android.support.annotation.StringRes;

/**
 * Created by Ryan on 05/03/2017.
 */

//TODO Explain what T is and how it Works
public interface BaseView<T> {


    void setPresenter(T presenter);


    void makeToast(@StringRes int message);

}