package com.bracketcove.postrainer.data.reminder;

import com.bracketcove.postrainer.data.viewmodel.Reminder;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * This interface describes the responsibilities and interactions between
 * Presenters and The ReminderRepository class.
 * Created by Ryan on 09/03/2017.
 */

public interface ReminderSource {

    /**
     *Completable: RxJava Observable which has two possible outcomes:
     * 1. It tells me that the operation completed
     * 2. It throws an error
     */
    Observable createReminder(Reminder reminder);

    Observable deleteReminder(Reminder reminder);

    Observable updateReminder(Reminder reminder);

    /**
     * Maybe: RxJava Observable which has three possible outcomes:
     * 1. It can return some data (in this case a list of Reminders)
     * 2. It can return nothing.
     * 3. It can throw an error
     */
    Observable<List<Reminder>> getReminders();

    /**
     * Single: RxJava Observable which has two possible outcomes:
     * 1. It can return single object (in this case a RealmReminder)
     * 2. It can throw an error
     */
    Observable<Reminder> getReminderById(Reminder reminder);
}
