package com.wiseassblog.common

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

@Throws(ParseException::class)
fun convertTime(hour: Int, minute: Int): String {
    val alarmTime = Integer.toString(hour) + ":" + Integer.toString(minute)
    val inFormat = SimpleDateFormat("HH:mm", Locale.getDefault()) //HH for hour of the day (0 - 23)

    val alarm: Date
    alarm = inFormat.parse(alarmTime)

    val outFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
    return outFormat.format(alarm).toLowerCase()
}

fun getFormattedDate(): String {
    val calendar = Calendar.getInstance()

    return "" + calendar.get(Calendar.DAY_OF_YEAR) +
            "" + calendar.get(Calendar.HOUR_OF_DAY) +
            "" + calendar.get(Calendar.MINUTE) +
            "" + calendar.get(Calendar.SECOND)
}

fun Boolean.toAlarmState(): String {
    return if (this) REMINDER_ON
    else REMINDER_OFF
}