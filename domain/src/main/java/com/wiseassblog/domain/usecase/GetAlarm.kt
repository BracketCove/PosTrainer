package com.wiseassblog.domain.usecase

import com.wiseassblog.common.ResultWrapper
import com.wiseassblog.domain.domainmodel.Alarm
import com.wiseassblog.domain.repository.IAlarmRepository

/**
 * Created by R_KAY on 5/23/2017.
 */

class GetAlarm(
    private val repo: IAlarmRepository
) : UseCaseWithParams<String, ResultWrapper<Exception, Alarm>>() {
    override suspend fun buildUseCase(params: String): ResultWrapper<Exception, Alarm> {
        return repo.getAlarmById(params)
    }
}
