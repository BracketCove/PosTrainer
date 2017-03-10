package com.bracketcove.postrainer;

import com.bracketcove.postrainer.reminderservice.FakeReminderService;
import com.bracketcove.postrainer.reminderservice.ReminderSource;

/**
 * Created by Ryan on 07/03/2017.
 */

public class ReminderInjection {
    public static ReminderSource provideReminderService() {
        return FakeReminderService.getInstance();
    }
}
