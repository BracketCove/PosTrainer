package com.bracketcove.postrainer.alarm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wiseassblog.domain.domainmodel.Alarm

class AlarmViewModel(
   private val alarmState: MutableLiveData<Alarm> = MutableLiveData()
) : ViewModel(),
    AlarmDetailContract.ViewModel {
    override fun setAlarm(alarm: Alarm) {
        alarmState.value = alarm
    }

    override fun getAlarm(): Alarm? = alarmState.value
}