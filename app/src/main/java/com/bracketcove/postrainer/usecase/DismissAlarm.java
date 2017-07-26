package com.bracketcove.postrainer.usecase;

import com.bracketcove.postrainer.data.alarmservice.AlarmManager;
import com.bracketcove.postrainer.data.alarmservice.AlarmService;

import io.reactivex.Completable;

/**
 *
 * Created by R_KAY on 5/23/2017.
 */

public class DismissAlarm implements UseCaseCompletable<Void> {

    private final AlarmManager alarmManager;

    public DismissAlarm(AlarmManager alarmManager) {
        this.alarmManager = alarmManager;
    }

    @Override
    public Completable runUseCase(Void... params) {
        return alarmManager.dismissAlarm();
    }
}
