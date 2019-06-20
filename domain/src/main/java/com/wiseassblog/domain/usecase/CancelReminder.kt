package com.wiseassblog.domain.usecase


import com.wiseassblog.common.ResultWrapper
import com.wiseassblog.domain.domainmodel.Reminder
import com.wiseassblog.domain.api.IReminderAPI
import com.wiseassblog.domain.repository.IReminderRepository

/**
 * 1. Tell reminderAPI to cancel the reminder
 * 2. Write reminder to database, with active = false
 *
 * Created by R_KAY on 5/23/2017.
 */

class CancelReminder(
    private val reminderAPI: IReminderAPI,
    private val repo: IReminderRepository
) : UseCaseWithParams<Reminder, ResultWrapper<Exception, Unit>>() {
    override suspend fun buildUseCase(params: Reminder): ResultWrapper<Exception, Unit> {
        val cancelResult = reminderAPI.cancelReminder(params)

        return if (cancelResult is ResultWrapper.Success) repo.updateReminders(params.copy(isActive = false))
        else cancelResult
    }

}
