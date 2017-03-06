package com.bracketcove.postrainer.util;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Ryan on 14/04/2016.
 */
public class TimeConverter {

    public static String convertTime (int hour, int minute){


        String unformattedTime = Integer.toString(hour) + ":" + Integer.toString(minute);
        DateFormat f1 = new SimpleDateFormat("HH:mm"); //HH for hour of the day (0 - 23)

        Date d = null;
        try {
            d = f1.parse(unformattedTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateFormat f2 = new SimpleDateFormat("h:mm a");
        return f2.format(d).toLowerCase();


    }

}
