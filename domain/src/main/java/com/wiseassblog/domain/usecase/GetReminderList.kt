package com.wiseassblog.domain.usecase

import com.wiseassblog.common.ResultWrapper
import com.wiseassblog.domain.domainmodel.Reminder
import com.wiseassblog.domain.repository.IReminderRepository

/**
 * Tested by my eyeball, because I like to live dangerously.
 *
 * Created by R_KAY on 5/23/2017.
 */

class GetReminderList(private val repo: IReminderRepository) : UseCase<ResultWrapper<Exception, List<Reminder>>>() {
    override suspend fun buildUseCase(): ResultWrapper<Exception, List<Reminder>> = repo.getReminders()

}
