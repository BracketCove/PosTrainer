package com.wiseassblog.domain.usecase


import com.wiseassblog.common.ResultWrapper
import com.wiseassblog.domain.domainmodel.Alarm
import com.wiseassblog.domain.repository.IAlarmRepository
import com.wiseassblog.domain.repository.IAlarmService

/**
 * 1. Tell alarmService to cancel the alarm
 * 2. Write alarm to database, with active = false
 *
 * Created by R_KAY on 5/23/2017.
 */

class CancelAlarm(
    private val service: IAlarmService,
    private val repo: IAlarmRepository
) : UseCaseWithParams<Alarm, ResultWrapper<Exception, Unit>>() {
    override suspend fun buildUseCase(params: Alarm): ResultWrapper<Exception, Unit> {
        val cancelResult = service.cancelAlarm(params)

        return if (cancelResult is ResultWrapper.Success) repo.updateAlarm(params.copy(isActive = false))
        else cancelResult
    }

}
