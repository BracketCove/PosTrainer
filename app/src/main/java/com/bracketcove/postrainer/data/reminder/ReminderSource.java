package com.bracketcove.postrainer.data.reminder;

import com.bracketcove.postrainer.BaseSource;
import com.bracketcove.postrainer.data.reminder.Reminder;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

/**
 * This interface describes the responsibilities and interactions between
 * Presenters and The ReminderRepository class.
 * Created by Ryan on 09/03/2017.
 */

public interface ReminderSource extends BaseSource {

    /**
     *Completable: RxJava Observable which has two possible outcomes:
     * 1. It tells me that the operation completed
     * 2. It throws an error
     */
    Completable createReminder(Reminder reminder);

    Completable deleteReminder(Reminder reminder);

    Completable updateReminder(Reminder reminder);

    /**
     * Maybe: RxJava Observable which has three possible outcomes:
     * 1. It can return some data (in this case a list of Reminders)
     * 2. It can return nothing.
     * 3. It can throw an error
     */
    Maybe<List<Reminder>> getReminders();

    /**
     * Single: RxJava Observable which has two possible outcomes:
     * 1. It can return single object (in this case a Reminder)
     * 2. It can throw an error
     */
    Single<Reminder> getReminderById(String reminderId);
}
