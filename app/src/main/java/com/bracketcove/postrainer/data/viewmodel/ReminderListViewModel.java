package com.bracketcove.postrainer.data.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

/**
 * Created by R_KAY on 6/22/2017.
 */

public class ReminderListViewModel extends ViewModel {

    /**
     * MutableLiveData allows us to use postValue(Object) and setValue() methods.
     * postValue() for background threads
     * setValue() for mainThread
     */
    public MutableLiveData<List<Reminder>> reminderList = new MutableLiveData<>();

}
