package com.wiseassblog.domain.repository

import com.wiseassblog.common.ResultWrapper
import com.wiseassblog.domain.domainmodel.Alarm

/**
 * This interface describes the responsibilities and interactions between
 * Presenters and The ReminderRepository class.
 * Created by Ryan on 09/03/2017.
 */

interface IAlarmService {

    suspend fun setAlarm(alarm: Alarm): ResultWrapper<Exception, Unit>

    suspend fun cancelAlarm(alarm: Alarm): ResultWrapper<Exception, Unit>

    suspend fun dismissAlarm(): ResultWrapper<Exception, Unit>

    suspend fun startAlarm(alarm: Alarm): ResultWrapper<Exception, Unit>
}
