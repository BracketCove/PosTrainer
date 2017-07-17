package com.bracketcove.postrainer.data.reminder;

import com.bracketcove.postrainer.data.viewmodel.Reminder;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

/**
 * This interface describes the responsibilities and interactions between
 * Presenters and The ReminderRepository class.
 * Created by Ryan on 09/03/2017.
 */

public interface ReminderSource {

    Completable deleteReminder(Reminder reminder);

    Completable updateReminder(Reminder reminder);

    Flowable<List<Reminder>> getReminders();

    Flowable<Reminder> getReminderById(String reminderId);
}
