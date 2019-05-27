package com.wiseassblog.domain.usecase

import com.wiseassblog.common.AlarmRepositoryException
import com.wiseassblog.common.ResultWrapper
import com.wiseassblog.domain.domainmodel.Alarm
import com.wiseassblog.domain.repository.IAlarmRepository
import com.wiseassblog.domain.repository.IAlarmService

/**
 * Created by R_KAY on 5/23/2017.
 */
class StartAlarm(private val service: IAlarmService,
                 private val repo: IAlarmRepository) : UseCaseWithParams<String, ResultWrapper<Exception, Unit>>() {
    override suspend fun buildUseCase(params: String): ResultWrapper<Exception, Unit> {
        val result = repo.getAlarmById(params)

        return when (result){
            is ResultWrapper.Success -> startAlarm(result.value)
            is ResultWrapper.Error -> ResultWrapper.build { throw AlarmRepositoryException }
        }
    }

    private suspend fun startAlarm(alarm: Alarm): ResultWrapper<Exception, Unit> = service.startAlarm(alarm)
}
