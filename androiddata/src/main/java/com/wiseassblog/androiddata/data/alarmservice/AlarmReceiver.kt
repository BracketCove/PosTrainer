package com.wiseassblog.androiddata.data.alarmservice

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import android.util.Log
import com.wiseassblog.androiddata.data.*


class AlarmReceiver : BroadcastReceiver() {

    var lock: PowerManager.WakeLock? = null

    override fun onReceive(context: Context?, intent: Intent?) {

        //Actions are String contants which can be used to discern intents
        when (intent?.action) {
            ACTION_ALARM_SET -> {
            }
            ACTION_ALARM_DISMISS -> releaseWakelock()
            ACTION_ALARM_CANCEL -> {
            }
            ACTION_ALARM_START -> {
                acquireWakelock(context)
                context?.startActivity(
                    Intent(
                        context, Class.forName(ACTIVITY_CLASS_NAME)
                    ).putExtra(ALARM_ID, intent?.getStringExtra(ALARM_ID))
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                )
            }
        }
        Log.d("RECEIVER", "Receiver is being called with ${intent?.action}")
    }

    private fun acquireWakelock(context: Context?) {
       lock = (context?.getSystemService(Context.POWER_SERVICE) as PowerManager).newWakeLock(
           PowerManager.PARTIAL_WAKE_LOCK
or PowerManager.ACQUIRE_CAUSES_WAKEUP or PowerManager.ON_AFTER_RELEASE,
            "Postrainer::WakeLock"
        )

        lock?.acquire(60000)
    }

    private fun releaseWakelock() {
        lock?.release()
    }
}