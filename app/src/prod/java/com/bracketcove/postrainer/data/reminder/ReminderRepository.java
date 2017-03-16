package com.bracketcove.postrainer.data.reminder;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Maybe;

/**
 * This is the real version of my service class, because it is in the "prod" source set.
 * Created by Ryan on 07/03/2017.
 */

public class ReminderRepository implements ReminderSource {
    @Inject
    ReminderRepository() {
    }

    @Override
    public void setReturnFail() {

    }

    @Override
    public void setReturnEmpty() {

    }

    @Override
    public Maybe<List<Reminder>> getReminderList() {
        List<Reminder> reminderList = new ArrayList<>();
        reminderList.add(new Reminder(10, 30, "title", false, false, false, 2002));

        return Maybe.just(reminderList);
    }
}
