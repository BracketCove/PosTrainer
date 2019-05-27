package com.bracketcove.postrainer.alarmlist

import com.wiseassblog.domain.domainmodel.Alarm

/**
 * Created by Ryan on 06/03/2017.
 */

interface AlarmListContract {
    interface View {
        fun setAlarmListData(alarmListData: List<Alarm>)

        fun setNoAlarmListDataFound()

        fun startAlarmDetailActivity(alarmId: String)

        fun startSettingsActivity()

        fun showMessage(msg: String)
    }
}

sealed class AlarmListEvent {
    object OnSettingsClick : AlarmListEvent()
    object OnCreateButtonClick : AlarmListEvent()
    data class OnAlarmIconClick(val alarm: Alarm) : AlarmListEvent()
    data class OnAlarmToggled(val isActive: Boolean, val alarm: Alarm) : AlarmListEvent()
    object OnStart : AlarmListEvent()
}