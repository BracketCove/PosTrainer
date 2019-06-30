package com.wiseassblog.androiddata.data.reminderapi

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.wiseassblog.androiddata.data.ACTION_REMINDER_START
import com.wiseassblog.androiddata.data.REMINDER_ID
import com.wiseassblog.androiddata.data.setReminderTime
import com.wiseassblog.common.ResultWrapper
import com.wiseassblog.domain.api.IReminderAPI
import com.wiseassblog.domain.domainmodel.Reminder
import java.util.*

/**
 *
 */
class ReminderApiImpl(
    private val context: Context
) : IReminderAPI {

    override suspend fun setReminder(reminder: Reminder): ResultWrapper<Exception, Unit> {
        val calendar = Calendar.getInstance().setReminderTime(reminder)

        //make sure you aren't setting an reminder for earlier today
        checkTime(calendar)

        val receiverIntent = Intent(context, ReminderBroadcastReceiver::class.java)
            .setAction(
                ACTION_REMINDER_START
            )
        receiverIntent.putExtra(REMINDER_ID, reminder.reminderId)
        receiverIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            Integer.parseInt(reminder.reminderId!!),
            receiverIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        //set alarm
        val aManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            aManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        } else {
            aManager.setExact(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        }

        return ResultWrapper.build { Unit }
    }

    override suspend fun cancelReminder(reminder: Reminder): ResultWrapper<Exception, Unit> {
        val intent = Intent(context, ReminderBroadcastReceiver::class.java)
        intent.putExtra(REMINDER_ID, reminder.reminderId)
            .setAction(
                ACTION_REMINDER_START
            )

        val receiverIntent = PendingIntent.getBroadcast(
            context,
            Integer.parseInt(reminder.reminderId),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val aManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        aManager.cancel(receiverIntent)
        return ResultWrapper.build { Unit }
    }

    //ensure that we don't set reminder for the past
    private fun checkTime(reminder: Calendar) {
        val now = Calendar.getInstance()
        if (reminder.before(now)) {
            val alarmForFollowingDay = reminder.timeInMillis + 86400000L
            reminder.timeInMillis = alarmForFollowingDay
        }
    }

}
