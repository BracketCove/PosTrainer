package com.bracketcove.postrainer.database;


/**
 *
 * Created by Ryan on 05/03/2017.
 */

public class AlarmService implements AlarmSource {

    public AlarmService() {

    }

    public static AlarmService getInstance () {
        return new AlarmService();
    }


    /* Setting an Alarm Dependencies:
        AlarmManager which comes from Activity (Or by Fragment)
        Intent
        PendingIntent
*/
}
