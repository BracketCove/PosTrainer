package com.bracketcove.postrainer.data.alarmservice;

import com.bracketcove.postrainer.data.viewmodel.Alarm;

import io.reactivex.Completable;

/**
 * This interface describes the responsibilities and interactions between
 * Presenters and The ReminderRepository class.
 * Created by Ryan on 09/03/2017.
 */

public interface AlarmManager {

    Completable setAlarm(Alarm alarm);

    Completable cancelAlarm(Alarm alarm);

    Completable dismissAlarm();

    Completable startAlarm(Alarm alarm);
}
