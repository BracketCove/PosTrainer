package com.wiseassblog.androiddata.data.alarmservice

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.os.PowerManager
import android.util.Log
import com.wiseassblog.androiddata.data.ACTION_ALARM_DISMISS
import com.wiseassblog.androiddata.data.ACTION_ALARM_START
import com.wiseassblog.androiddata.data.ALARM_ID
import com.wiseassblog.androiddata.data.RECEIVER_CLASS_NAME

/**
 * - Has Wakelock
 * - Can send broadcasts to AlarmReceiver
 * - Receives intents from AlarmReceiver with specific actions
 *
 */
class AlarmService : Service() {

override fun onBind(intent: Intent?): IBinder? {
    return null
}

override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
    Log.d("ALARM_SERVICE", "Service is being called with ${intent?.action}")
    when (intent?.action) {
        ACTION_ALARM_START -> {
            (getSystemService(Context.POWER_SERVICE) as PowerManager).newWakeLock(
                PowerManager.PARTIAL_WAKE_LOCK,
                "Postrainer::WakeLock"
            ).acquire(60000)

            val activityIntent = Intent(this, Class.forName(RECEIVER_CLASS_NAME))
            activityIntent.putExtra(ALARM_ID, intent.getStringExtra(ALARM_ID))
            activityIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(activityIntent)
        }

        ACTION_ALARM_DISMISS -> {
             (getSystemService(Context.POWER_SERVICE) as PowerManager).newWakeLock(
                PowerManager.PARTIAL_WAKE_LOCK,
                "Postrainer::WakeLock"
            ).apply {
                 if (isHeld) release()
             }

            this.stopSelf()
        }
    }

    return super.onStartCommand(intent, flags, startId)
}
}