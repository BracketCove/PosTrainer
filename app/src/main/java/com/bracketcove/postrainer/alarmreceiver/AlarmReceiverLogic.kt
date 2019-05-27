package com.bracketcove.postrainer.alarmreceiver

import android.util.Log
import com.bracketcove.postrainer.BaseLogic
import com.bracketcove.postrainer.dependencyinjection.AndroidDependencyProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * Created by Ryan on 05/03/2017.
 */

class AlarmReceiverLogic(
    private val view: AlarmReceiverContract.View,
    private val provider: AndroidDependencyProvider,
    dispatcher: CoroutineDispatcher
) : BaseLogic<AlarmReceiverEvent>(dispatcher) {

    override val coroutineContext: CoroutineContext
        get() = main + jobTracker

    override fun handleEvent(eventType: AlarmReceiverEvent) {
        when (eventType){
            is AlarmReceiverEvent.OnStart -> onStart(eventType.alarmId)
            is AlarmReceiverEvent.OnDismissed -> onAlarmDismissClick(eventType.alarmId)
        }
    }

    private fun onStart(alarmId: String) = launch {
        Log.d("ALARM", "onStart() was called with $alarmId" )

        provider.startAlarm.execute(alarmId)
    }

    private fun onAlarmDismissClick(alarmId: String) = launch {
        provider.dismissAlarm.execute(alarmId)
        view.finish()
    }
}
