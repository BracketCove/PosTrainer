package com.bracketcove.postrainer.data.reminder;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
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
    Completable createReminder(String reminderId);

    Completable deleteReminder(String reminderId);

    Completable updateReminder(RealmReminder reminder);

    /**
     * Maybe: RxJava Observable which has three possible outcomes:
     * 1. It can return some data (in this case a list of Reminders)
     * 2. It can return nothing.
     * 3. It can throw an error
     */
    Maybe<List<RealmReminder>> getReminders();

    /**
     * Single: RxJava Observable which has two possible outcomes:
     * 1. It can return single object (in this case a RealmReminder)
     * 2. It can throw an error
     */
    Single<RealmReminder> getReminderById(String reminderId);
}
