package com.wiseassblog.domain

import com.wiseassblog.common.ReminderServiceException
import com.wiseassblog.common.ResultWrapper
import com.wiseassblog.domain.repository.IReminderRepository
import com.wiseassblog.domain.api.IReminderAPI
import com.wiseassblog.domain.usecase.SetReminder
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

/**
 *
 * Set Reminder:
 * User has toggled an Reminder to be started at a particular time.
 * 1. Tell ReminderService to set the Reminder
 * 2a. Reminder has been set, and should now be written to the database
 * 2b. For some reason setting the Reminder did not work; avoid writing to database
 * 3. Profit?
 *
 **/

class SetReminderTest {

    val repo:IReminderRepository = mockk()
    val reminderAPI: IReminderAPI = mockk()

    val set = SetReminder(reminderAPI, repo)

    @BeforeEach
    fun build() {
        clearAllMocks()
    }

    /**
     * a:
     * 1. Tell reminderAPI to set an Reminder
     * 2. Write that Reminder to database with isActive = true
     * 3. Profit?
     */
    @Test
    fun `Reminder Set`() = runBlockingTest {
        coEvery { reminderAPI.setReminder(getReminder(active = false)) } returns ResultWrapper.build { Unit }
        coEvery { repo.updateReminders(getReminder(active = true)) } returns ResultWrapper.build { Unit }

        set.execute(getReminder(active = false))

        coVerify { reminderAPI.setReminder(getReminder(active = false)) }
        coVerify { repo.updateReminders(getReminder(active = true)) }
    }

    /**
     * a:
     * 1. Tell reminderAPI to set an Reminder
     * 2. Definitely not Profit :(
     */
    @Test
    fun `Reminder not set`() = runBlockingTest {
        coEvery { reminderAPI.setReminder(getReminder(active = false)) } returns ResultWrapper.build {  throw ReminderServiceException }

        set.execute(getReminder(active = false))

        coVerify { reminderAPI.setReminder(getReminder(active = false)) }
        //ensure this was not called:
        coVerify(exactly = 0) { repo.updateReminders(getReminder(active = true)) }
    }

}