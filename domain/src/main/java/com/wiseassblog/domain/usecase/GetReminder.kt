package com.wiseassblog.domain.usecase

import com.wiseassblog.common.ResultWrapper
import com.wiseassblog.domain.domainmodel.Reminder
import com.wiseassblog.domain.repository.IReminderRepository

/**
 * Created by R_KAY on 5/23/2017.
 */

class GetReminder(
    private val repo: IReminderRepository
) : UseCaseWithParams<String, ResultWrapper<Exception, Reminder>>() {
    override suspend fun buildUseCase(params: String): ResultWrapper<Exception, Reminder> {
        return repo.getReminderById(params)
    }
}
