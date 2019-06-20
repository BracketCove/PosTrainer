package com.wiseassblog.domain.usecase

import com.wiseassblog.common.ResultWrapper
import com.wiseassblog.domain.domainmodel.Reminder
import com.wiseassblog.domain.api.IReminderAPI
import com.wiseassblog.domain.repository.IReminderRepository

/**
 * Created by R_KAY on 5/23/2017.
 */

class SetReminder(
    private val reminderAPI: IReminderAPI,
    private val repo: IReminderRepository
) : UseCaseWithParams<Reminder, ResultWrapper<Exception, Unit>>() {
    override suspend fun buildUseCase(params: Reminder): ResultWrapper<Exception, Unit> {

        val setReminderResult = reminderAPI.setReminder(params)
        return if (setReminderResult is ResultWrapper.Success) repo.updateReminders(params.copy(isActive = true))
        else setReminderResult

    }

}
