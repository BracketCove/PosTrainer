package com.wiseassblog.domain.repository

import com.wiseassblog.common.ResultWrapper
import com.wiseassblog.domain.domainmodel.Alarm

interface IAlarmRepository {

    suspend fun getAlarms(): ResultWrapper<Exception, List<Alarm>>

    suspend fun deleteAlarm(alarm: Alarm): ResultWrapper<Exception, Unit>

    suspend fun updateAlarm(alarm: Alarm): ResultWrapper<Exception, Unit>

    suspend fun getAlarmById(alarmId: String): ResultWrapper<Exception, Alarm>
}