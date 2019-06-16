package com.wiseassblog.domain.repository

import com.wiseassblog.common.ResultWrapper
import com.wiseassblog.domain.domainmodel.Reminder

interface IReminderRepository {

    suspend fun getReminders(): ResultWrapper<Exception, List<Reminder>>

    suspend fun deleteReminder(reminder: Reminder): ResultWrapper<Exception, Unit>

    suspend fun updateReminders(reminder: Reminder): ResultWrapper<Exception, Unit>

    suspend fun getReminderById(alarmId: String): ResultWrapper<Exception, Reminder>
}