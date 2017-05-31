package com.bracketcove.postrainer.usecase;

import com.bracketcove.postrainer.data.alarm.AlarmService;
import com.bracketcove.postrainer.data.viewmodel.Reminder;

import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 *
 * Created by R_KAY on 5/23/2017.
 */

public class DismissAlarm implements UseCase.Request {

    private final AlarmService alarmService;

    public DismissAlarm(AlarmService alarmService) {
        this.alarmService = alarmService;
    }


    @Override
    public Observable runUseCase() {
        return alarmService.dismissAlarm();
    }
}
