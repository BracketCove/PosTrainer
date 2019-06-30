package com.wiseassblog.androiddata.data

import android.os.Handler
import android.os.HandlerThread

/**
 * This class allows the ReminderBroadcastReceiver to live a bit longer than
 * when its onReceive returns
 */
object AsyncHandler {
    private val sHandlerThread = HandlerThread("AsyncHandler")
    private val sHandler: Handler

    init {
        sHandlerThread.start()
        sHandler = Handler(sHandlerThread.looper)
    }

    fun post(r: Runnable) {
        sHandler.post(r)
    }
}