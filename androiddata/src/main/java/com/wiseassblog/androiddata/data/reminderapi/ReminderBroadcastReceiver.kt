package com.wiseassblog.androiddata.data.reminderapi

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import android.util.Log
import com.wiseassblog.androiddata.data.ACTION_REMINDER_DISMISSED
import com.wiseassblog.androiddata.data.ACTION_REMINDER_START
import com.wiseassblog.androiddata.data.AsyncHandler
import com.wiseassblog.androiddata.data.REMINDER_ID
import com.wiseassblog.androiddata.data.reminderdatabase.ReminderDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class ReminderBroadcastReceiver : BroadcastReceiver() {

    var lock: PowerManager.WakeLock? = null
    val jobTracker = Job()
    val scope = CoroutineScope(Main + jobTracker)

    override fun onReceive(context: Context?, intent: Intent?) {

        //Actions are String contants which can be used to discern intents
        when (intent?.action) {
            ACTION_REMINDER_DISMISSED -> {
            }
            ACTION_REMINDER_START -> {
                context?.let { startAlarm(it, intent?.getStringExtra(REMINDER_ID)) }
            }
        }
        Log.d("RECEIVER", "Receiver is being called with ${intent?.action}")
    }

    private fun startAlarm(context: Context, reminderId: String) {
        val result = goAsync()

        lock = (context?.getSystemService(Context.POWER_SERVICE) as PowerManager).newWakeLock(
            PowerManager.PARTIAL_WAKE_LOCK,
            "Postrainer::WakeLock"
        )

        lock?.acquire(60000)

        AsyncHandler.post(object : Runnable {
            override fun run() {
                NotificationManager.showNotification(reminderId, context)
                updateAlarm(reminderId)
                result.finish()
                lock?.release()
            }
        })
    }

    private fun updateAlarm(reminderId: String) {
        scope.launch {
            ReminderDatabase.makeReminderInactive(reminderId)
        }
    }
}