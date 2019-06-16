package com.wiseassblog.androiddata.data.reminderapi

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.wiseassblog.androiddata.data.ACTION_REMINDER_DISMISSED
import com.wiseassblog.androiddata.data.ACTION_REMINDER_SHOW
import com.wiseassblog.androiddata.data.REMINDER_ID
import com.wiseassblog.androiddata.data.reminderdatabase.ReminderDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * - Shows the Notification, and also updates the Database when the notificaiton is fired.
 * - Can send broadcasts to ReminderBroadcastReceiver
 * - Receives intents from ReminderBroadcastReceiver with specific actions
 *
 */
class ReminderService : Service() {

    private val jobTracker = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        jobTracker.cancel()
        super.onDestroy()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("REMINDER_SERVICE", "Service is being called with ${intent?.action}")
        when (intent?.action) {
            ACTION_REMINDER_SHOW -> {
                val reminderId = intent.getStringExtra(REMINDER_ID)
                coroutineScope.launch { ReminderDatabase.makeReminderInactive(reminderId) }
                stopSelf()

            }
            ACTION_REMINDER_DISMISSED -> onDismissed(intent.getStringExtra(REMINDER_ID))
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun onDismissed(stringExtra: String?) {
        stopSelf()
    }
}