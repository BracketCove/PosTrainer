package com.wiseassblog.domain.usecase

import com.wiseassblog.common.ResultWrapper
import com.wiseassblog.domain.domainmodel.Reminder
import com.wiseassblog.domain.repository.IReminderRepository

/**
 * Created by R_KAY on 5/23/2017.
 */

class UpdateOrCreateReminder(private val repo: IReminderRepository) :
    UseCaseWithParams<Reminder, ResultWrapper<Exception, Unit>>() {
    override suspend fun buildUseCase(reminder: Reminder): ResultWrapper<Exception, Unit> = repo.updateReminders(reminder)
}
