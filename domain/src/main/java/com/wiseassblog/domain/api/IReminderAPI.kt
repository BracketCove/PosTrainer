package com.wiseassblog.domain.api

import com.wiseassblog.common.ResultWrapper
import com.wiseassblog.domain.domainmodel.Reminder

/**
 * This interface describes the responsibilities and interactions between
 * Presenters and The ReminderRepository class.
 * Created by Ryan on 09/03/2017.
 */

interface IReminderAPI {

    suspend fun setReminder(reminder: Reminder): ResultWrapper<Exception, Unit>

    suspend fun cancelReminder(reminder: Reminder): ResultWrapper<Exception, Unit>

}
