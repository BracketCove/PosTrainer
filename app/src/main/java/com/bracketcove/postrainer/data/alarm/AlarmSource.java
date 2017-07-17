package com.bracketcove.postrainer.data.alarm;

import com.bracketcove.postrainer.data.viewmodel.Reminder;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;

/**
 * This interface describes the responsibilities and interactions between
 * Presenters and The ReminderRepository class.
 * Created by Ryan on 09/03/2017.
 */

public interface AlarmSource {

    Completable setAlarm(Reminder reminder);

    Completable cancelAlarm(Reminder reminder);

    Completable dismissAlarm();

    Completable startAlarm(Reminder reminder);
}
