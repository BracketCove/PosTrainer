package com.wiseassblog.domain

import com.wiseassblog.common.ResultWrapper
import com.wiseassblog.domain.api.IReminderAPI
import com.wiseassblog.domain.repository.IReminderRepository
import com.wiseassblog.domain.usecase.CancelReminder
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CancelReminderTest {

    val reminderAPI: IReminderAPI = mockk()
    val repo: IReminderRepository = mockk()

    val cancel = CancelReminder(reminderAPI, repo)

    @BeforeEach
    fun build(){
        clearAllMocks()
    }

    /**
     * 1. Tell reminderAPI to cancel the Reminder
     * 2. Write Reminder to database, with active = false
     */
    @Test
    fun `On Reminder Cancelled`() = runBlockingTest {
        coEvery { reminderAPI.cancelReminder(getReminder()) } returns ResultWrapper.build { Unit }
        coEvery { repo.updateReminders(getReminder(active = false)) } returns ResultWrapper.build { Unit }

        cancel.execute(getReminder())

        coVerify { reminderAPI.cancelReminder(getReminder()) }
        coVerify { repo.updateReminders(getReminder(active = false)) }
    }
}