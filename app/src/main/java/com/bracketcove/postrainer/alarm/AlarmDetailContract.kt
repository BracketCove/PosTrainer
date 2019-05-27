package com.bracketcove.postrainer.alarm

import com.wiseassblog.domain.domainmodel.Alarm

/**
 * Created by Ryan on 06/03/2017.
 */

interface AlarmDetailContract {
    interface View  {
        fun setAlarmTitle(title:String)

        fun setVibrateOnly(vibrateOnly: Boolean)

        fun setRenewAutomatically(renewAutomatically: Boolean)

        fun setPickerTime(hour: Int, minute: Int)

        fun startAlarmListActivity()

        fun showMessage(msg: String)

        fun showDeleteConfirm()

        fun getState(): Alarm
    }

    interface ViewModel {
        fun getAlarm(): Alarm?
        fun setAlarm(alarm: Alarm)
    }
}

sealed class AlarmDetailEvent {
    object OnBackIconPress : AlarmDetailEvent()
    object OnDoneIconPress : AlarmDetailEvent()
    object OnDeleteIconPress : AlarmDetailEvent()
    object OnDeleteConfirmed : AlarmDetailEvent()
    object OnStart : AlarmDetailEvent()
}
