package com.bracketcove.postrainer.usecase;

import com.bracketcove.postrainer.data.reminder.ReminderService;
import com.bracketcove.postrainer.data.viewmodel.Reminder;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;

/**
 * Created by R_KAY on 5/23/2017.
 */

public class UpdateOrCreateReminder implements UseCaseCompletable<Reminder> {

    private final ReminderService reminderService;

    public UpdateOrCreateReminder(ReminderService reminderService) {
        this.reminderService = reminderService;
    }

    @Override
    public Completable runUseCase(Reminder... params) {
        return reminderService.updateReminder(params[0]);
    }
}
