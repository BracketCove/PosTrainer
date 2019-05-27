package com.wiseassblog.domain.usecase

import com.wiseassblog.common.ResultWrapper
import com.wiseassblog.domain.domainmodel.Alarm
import com.wiseassblog.domain.repository.IAlarmRepository

/**
 *
 * Tested by my eyeball, because I like to live dangerously.
 * Created by R_KAY on 5/23/2017.
 */

class DeleteAlarm(private val repo: IAlarmRepository) : UseCaseWithParams<Alarm, ResultWrapper<Exception, Unit>>() {

    override suspend fun buildUseCase(params: Alarm): ResultWrapper<Exception, Unit> = repo.deleteAlarm(params)
}
