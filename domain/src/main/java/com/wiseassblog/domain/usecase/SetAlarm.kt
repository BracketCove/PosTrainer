package com.wiseassblog.domain.usecase

import com.wiseassblog.common.ResultWrapper
import com.wiseassblog.domain.domainmodel.Alarm
import com.wiseassblog.domain.repository.IAlarmRepository
import com.wiseassblog.domain.repository.IAlarmService

/**
 * Created by R_KAY on 5/23/2017.
 */

class SetAlarm(
    private val service: IAlarmService,
    private val repo: IAlarmRepository
) : UseCaseWithParams<Alarm, ResultWrapper<Exception, Unit>>() {
    override suspend fun buildUseCase(params: Alarm): ResultWrapper<Exception, Unit> {

        val setAlarmResult = service.setAlarm(params)
        return if (setAlarmResult is ResultWrapper.Success) repo.updateAlarm(params.copy(isActive = true))
        else setAlarmResult

    }

}
