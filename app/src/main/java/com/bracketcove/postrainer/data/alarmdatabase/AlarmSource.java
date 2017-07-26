package com.bracketcove.postrainer.data.alarmdatabase;

import com.bracketcove.postrainer.data.viewmodel.Alarm;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

/**
 * This interface describes the responsibilities and interactions between
 * Presenters and The ReminderRepository class.
 * Created by Ryan on 09/03/2017.
 */

public interface AlarmSource {

    Completable deleteAlarm(Alarm alarm);

    Completable updateAlarm(Alarm alarm);

    Flowable<List<Alarm>> getAlarms();

    Flowable<Alarm> getAlarmsById(String reminderId);
}
