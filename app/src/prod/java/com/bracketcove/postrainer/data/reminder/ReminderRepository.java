package com.bracketcove.postrainer.data.reminder;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

/**
 * This is the real version of my service class, because it is in the "prod" source set.
 * Created by Ryan on 07/03/2017.
 */

public class ReminderRepository implements ReminderSource {

    public ReminderRepository() {
    }

    @Override
    public void setReturnFail() {

    }

    @Override
    public void setReturnEmpty() {

    }

    @Override
    public Maybe<List<Reminder>> getReminders() {
        List<Reminder> reminderList = new ArrayList<>();
        reminderList.add(new Reminder(10, 30, "title", false, false, false, "2002"));

        return Maybe.just(reminderList);
    }

    @Override
    public Completable createReminder(Reminder reminder) {
        return null;
    }

    @Override
    public Completable deleteReminder(Reminder reminder) {
        return null;
    }

    @Override
    public Completable updateReminder(Reminder reminder) {
        return null;
    }


    @Override
    public Single<Reminder> getReminderById(String reminderId) {
        return null;
    }
}
