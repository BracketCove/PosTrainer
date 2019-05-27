package com.bracketcove.postrainer.alarmreceiver.buildlogic

import com.bracketcove.postrainer.alarmreceiver.AlarmReceiverActivity
import com.bracketcove.postrainer.alarmreceiver.AlarmReceiverLogic
import com.bracketcove.postrainer.dependencyinjection.AndroidDependencyProvider
import kotlinx.coroutines.Dispatchers

object AlarmReceiverInjector {

    internal fun provideLogic(activity: AlarmReceiverActivity): AlarmReceiverLogic {
        return AlarmReceiverLogic(
            activity,
            AndroidDependencyProvider(activity.application),
            Dispatchers.Main
        )
    }
}