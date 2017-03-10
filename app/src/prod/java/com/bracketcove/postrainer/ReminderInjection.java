package com.bracketcove.postrainer;

import com.bracketcove.postrainer.data.AlarmService;
import com.bracketcove.postrainer.data.AlarmSource;

/**
 * Created by Ryan on 07/03/2017.
 */

public class ReminderInjection {
    public static ReminderInjection provideReminderService() {
        return ReminderInjection.getInstance();
    }
}
