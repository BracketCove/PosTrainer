package com.bracketcove.postrainer.data.alarm;

import com.bracketcove.postrainer.data.reminder.RealmReminder;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

/**
 * This interface describes the responsibilities and interactions between
 * Presenters and The ReminderRepository class.
 * Created by Ryan on 09/03/2017.
 */

public interface AlarmSource {

    Completable setAlarm(RealmReminder reminder);

    Completable cancelAlarm(RealmReminder reminder);

    void stopAlarm();

    void startAlarm(RealmReminder reminder);

    void releaseWakeLock();
}
