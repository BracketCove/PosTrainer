package com.wiseassblog.domain.usecase

import com.wiseassblog.common.ResultWrapper
import com.wiseassblog.domain.domainmodel.Reminder
import com.wiseassblog.domain.repository.IReminderRepository

/**
 *
 * Tested by my eyeball, because I like to live dangerously.
 * Created by R_KAY on 5/23/2017.
 */

class DeleteReminder(private val repo: IReminderRepository) : UseCaseWithParams<Reminder, ResultWrapper<Exception, Unit>>() {

    override suspend fun buildUseCase(params: Reminder): ResultWrapper<Exception, Unit> = repo.deleteReminder(params)
}
