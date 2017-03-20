package com.bracketcove.postrainer.data.reminder;

import com.bracketcove.postrainer.R;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

/**
 * This is a Fake Service. I use it during Unit Tests, as I don't want a real service during such
 * tests.
 * Created by Ryan on 07/03/2017.
 */
@Singleton
public class FakeReminderRepository implements ReminderSource {
    boolean returnFailure = false;
    boolean returnEmpty = false;

    private static final String TITLE = "Coffee Break";

    private static final int MINUTE = 30;

    private static final int HOUR = 10;

    //TODO: fix this test data to look the same as implementation would
    private static final String CREATION_DATE = "111111111111111";

    private static final Reminder testReminder = new Reminder(HOUR,
            MINUTE,
            TITLE,
            false,
            false,
            false,
            CREATION_DATE
    );

     @Inject public FakeReminderRepository() {
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
        reminders.add(testReminder);

        return Maybe.just(reminders);
    }

    @Override
    public Single<Reminder> getReminderById(String reminderId) {
        if (returnFailure) {
            return Single.error(new Exception());
        }
        return Single.just(testReminder);
    }
}
