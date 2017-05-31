package com.bracketcove.postrainer.usecase;

import com.bracketcove.postrainer.data.reminder.ReminderService;
import com.bracketcove.postrainer.data.viewmodel.Reminder;

import io.reactivex.Observable;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Retrieve a specific Reminder from the Database, according to a reminderId which is passed in to
 * this Use Case.
 * - If a Reminder is found, return it to the Presenter.
 * - If no Reminder is found, throw NoReminderFoundException
 * Created by R_KAY on 5/23/2017.
 */

public class GetReminder implements UseCase.RequestModel {

    /**
     *Reminder Service is a Facade/Repository Pattern which Abstracts Realm from the rest of my the
     App
     */
    private final ReminderService reminderService;

    public GetReminder(ReminderService reminderService) {
        this.reminderService = reminderService;
    }

    @Override
    public Observable runUseCase(Reminder reminder) {
        return reminderService.getReminderById(reminder);
    }

}
