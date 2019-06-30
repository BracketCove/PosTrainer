package com.wiseassblog.androiddata.data.reminderapi

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.wiseassblog.androiddata.R
import com.wiseassblog.androiddata.data.*

object NotificationManager {
    internal fun showNotification(reminderId: String, context: Context) {
        createNotificationChannel(context)

        val notification = NotificationCompat.Builder(context, CHANNEL_REMINDER)
            .setContentTitle(NOTIFICATION_DISMISS_TITLE)
            .setContentText(NOTIFICATION_CONTENT)
            .setColor(Color.BLUE)
            .setSmallIcon(R.drawable.ic_alarm_black_48dp)
            .setOngoing(true)
            //cancel on click
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_LIGHTS)
            .setWhen(0)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setLocalOnly(true)

        notification.setContentIntent(
            getDismissIntent(context, reminderId)
        )

        notification.setPriority(NotificationCompat.PRIORITY_MAX)

        NotificationManagerCompat.from(context).notify(
            NOTIFICATION_DISMISS_ID,
            notification.build()
        )
    }

    private fun getDismissIntent(context: Context, reminderId: String): PendingIntent {
        return Intent(context, ReminderService::class.java).let {
            it.action = ACTION_REMINDER_DISMISSED
            it.putExtra(REMINDER_ID, reminderId)
            PendingIntent.getService(
                context,
                NOTIFICATION_DISMISS_ID,
                it,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel
            val name = CHANNEL_REMINDER
            val descriptionText = CHANNEL_DISMISS_DESCRIPTION
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_REMINDER, CHANNEL_DISMISS_NAME, importance)
            channel.description = descriptionText
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}