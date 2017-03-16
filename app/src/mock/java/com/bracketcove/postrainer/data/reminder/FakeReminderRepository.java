package com.bracketcove.postrainer.data.reminder;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Maybe;

/**
 * This is a Fake Service. I use it during Unit Tests, as I don't want a real service during such
 * tests.
 * Created by Ryan on 07/03/2017.
 */
@Singleton
public class FakeReminderRepository implements ReminderSource {
    boolean returnFailure = false;
    boolean returnEmpty = false;

     @Inject FakeReminderRepository() {

    }

    public static FakeReminderRepository getInstance() {
        return new FakeReminderRepository();
    }

    @Override
    public void setReturnFail() {
        this.returnFailure = true;
    }

    @Override
    public void setReturnEmpty() {
        this.returnEmpty = true;
    }

    @Override
    public Completable createReminder(Reminder reminder) {
        if (returnFailure) {
            return Completable.error(new Exception());
        }
        return Completable.complete();
    }

    @Override
    public Completable deleteReminder(Reminder reminder) {
        if (returnFailure) {
            return Completable.error(new Exception());
        }
        return Completable.complete();
    }

    @Override
    public Completable updateReminder(Reminder reminder) {
        if (returnFailure) {
            return Completable.error(new Exception());
        }
        return Completable.complete();
    }

    @Override
    public Maybe<List<Reminder>> getReminders() {
        if (returnFailure) {
            return Maybe.error(new Exception());
        } else if (returnEmpty) {
            return Maybe.empty();
        }

        List<Reminder> reminders = new ArrayList<>();
        reminders.add(new Reminder(15, 15, "title", true, true, true, 15));

        return Maybe.just(reminders);
    }
}
