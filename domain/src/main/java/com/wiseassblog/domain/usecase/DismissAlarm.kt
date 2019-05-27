package com.wiseassblog.domain.usecase

import com.wiseassblog.common.ResultWrapper
import com.wiseassblog.domain.domainmodel.Alarm
import com.wiseassblog.domain.repository.IAlarmRepository
import com.wiseassblog.domain.repository.IAlarmService

/**
 *Dismiss Alarm:
 * 1. Tell system services (MediaPlayer, Vibrator, etc.) to STFU
 * 2. Retrieve alarm from database
 * 3a. Alarm alarm is rewritten to database inactive
 * 3b. Alarm is reset for 24hr ahead in time
 * 4b. Alarm is rewritten to database active
 */
class DismissAlarm(private val service: IAlarmService,
                   private val repo: IAlarmRepository) :
    UseCaseWithParams<String, ResultWrapper<Exception, Unit>>() {
    override suspend fun buildUseCase(params: String): ResultWrapper<Exception, Unit>{
       val dismissResult = service.dismissAlarm()

       return when (dismissResult){
            is ResultWrapper.Success -> onAlarmDismissed(params)
           is ResultWrapper.Error -> dismissResult
        }
    }

    /**
     * Retrieves alarm by id.
     */
    private suspend fun onAlarmDismissed(alarmId:String): ResultWrapper<Exception, Unit>{
        val getAlarmResult = repo.getAlarmById(alarmId)

       return when (getAlarmResult) {
        is ResultWrapper.Success -> onAlarmRetrieved(getAlarmResult.value)
            is ResultWrapper.Error -> getAlarmResult
        }
    }

    /**
     * If alarm is set to renew automatically, there is no need to update the repository.
     * If alarm is not set to renew automatically, change status to inactive and write to repo
     */
    private suspend fun onAlarmRetrieved(alarm: Alarm): ResultWrapper<Exception, Unit> {
        return if (alarm.isRenewAutomatically) ResultWrapper.build { Unit }
        else repo.updateAlarm(alarm.copy(isActive = false))
    }
}
