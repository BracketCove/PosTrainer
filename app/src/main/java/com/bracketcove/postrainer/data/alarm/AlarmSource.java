package com.bracketcove.postrainer.data.alarm;

import com.bracketcove.postrainer.data.reminder.RealmReminder;
import com.bracketcove.postrainer.data.viewmodel.Reminder;

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

    Completable setAlarm(Reminder reminder);

    Completable cancelAlarm(Reminder reminder);

    void stopAlarm();

    void startAlarm(Reminder reminder);

    void releaseWakeLock();
}
