package com.bracketcove.postrainer.data.reminder;


/**
 *
 * Created by Ryan on 05/03/2017.
 */

public class ReminderService {

    public ReminderService() {

    }

    public static ReminderService getInstance () {
        return new ReminderService();
    }

    /* Setting an Alarm Dependencies:
        AlarmManager which comes from Activity (Or by Fragment)
        Intent
        PendingIntent*/
}
