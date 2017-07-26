package com.bracketcove.postrainer.usecase;

import com.bracketcove.postrainer.data.alarmdatabase.AlarmDatabase;
import com.bracketcove.postrainer.data.alarmdatabase.AlarmSource;
import com.bracketcove.postrainer.data.viewmodel.Alarm;

import io.reactivex.Completable;

/**
 * Created by R_KAY on 5/23/2017.
 */

public class UpdateOrCreateAlarm implements UseCaseCompletable<Alarm> {

    private final AlarmSource alarmSource;

    public UpdateOrCreateAlarm(AlarmSource alarmSource) {
        this.alarmSource = alarmSource;
    }

    @Override
    public Completable runUseCase(Alarm... params) {
        return alarmSource.updateAlarm(params[0]);
    }
}
