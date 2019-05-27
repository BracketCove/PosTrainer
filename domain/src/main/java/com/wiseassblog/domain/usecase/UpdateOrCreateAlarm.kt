package com.wiseassblog.domain.usecase

import com.wiseassblog.common.ResultWrapper
import com.wiseassblog.domain.domainmodel.Alarm
import com.wiseassblog.domain.repository.IAlarmRepository

/**
 * Created by R_KAY on 5/23/2017.
 */

class UpdateOrCreateAlarm(private val repo: IAlarmRepository) :
    UseCaseWithParams<Alarm, ResultWrapper<Exception, Unit>>() {
    override suspend fun buildUseCase(alarm: Alarm): ResultWrapper<Exception, Unit> = repo.updateAlarm(alarm)
}
