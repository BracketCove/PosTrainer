package com.bracketcove.postrainer.alarmreceiver

/**
 * Created by Ryan on 05/03/2017.
 */

interface AlarmReceiverContract {
    interface View {
        fun finish()
    }
}

sealed class AlarmReceiverEvent {
    data class OnStart(val alarmId: String): AlarmReceiverEvent()
    data class OnDismissed(val alarmId: String): AlarmReceiverEvent()
}