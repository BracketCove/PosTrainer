package com.wiseassblog.androiddata.data

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        Log.d("RECEIVER", "Receiver is being called")
        intent?.setClass(context, Class.forName(RECEIVER_CLASS_NAME))
        intent?.setAction(Intent.ACTION_MAIN)
        intent?.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context?.startActivity(intent)
    }
}