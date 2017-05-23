package com.bracketcove.postrainer.reminderdetail.usecase;

import com.bracketcove.postrainer.base.UseCase;
import com.bracketcove.postrainer.data.reminder.ReminderService;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Retrieve a specific Reminder from the Database, according to a reminderId which is passed in to
 * this Use Case.
 * - If a Reminder is found, return it to the Presenter.
 * - If no Reminder is found, throw NoReminderFoundException
 * Created by R_KAY on 5/23/2017.
 */

public class GetReminder extends UseCase<GetReminder.RequestModel, GetReminder.ResponseModel> {

    //Some people may have called this ReminderRepository (just fyi).
    private final ReminderService reminderService;

    public GetReminder(ReminderService reminderService) {
        this.reminderService = reminderService;
    }

    @Override
    protected void executeUseCase(RequestModel requestModel) {

    }

    //this is where
    public static final class RequestModel implements UseCase.RequestValues {

    }

    public static final class ResponseModel implements UseCase.ResponseValue {

    }
}
