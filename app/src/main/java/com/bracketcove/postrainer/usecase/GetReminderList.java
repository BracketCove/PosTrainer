package com.bracketcove.postrainer.usecase;

import com.bracketcove.postrainer.data.reminder.ReminderService;
import com.bracketcove.postrainer.data.viewmodel.Reminder;

import org.reactivestreams.Publisher;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;

/**
 * Created by R_KAY on 5/23/2017.
 */

public class GetReminderList implements UseCase<List<Reminder>, Void> {

    private final ReminderService reminderService;

    public GetReminderList(ReminderService reminderService) {
        this.reminderService = reminderService;
    }

    @Override
    public Flowable<List<Reminder>> runUseCase(Void... params) {
        return reminderService.getReminders();
    }
}
