package com.wiseassblog.domain

import com.wiseassblog.common.AlarmServiceException
import com.wiseassblog.common.ResultWrapper
import com.wiseassblog.domain.repository.IAlarmRepository
import com.wiseassblog.domain.repository.IAlarmService
import com.wiseassblog.domain.usecase.SetAlarm
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

/**
 *
 * Set Alarm:
 * User has toggled an alarm to be started at a particular time.
 * 1. Tell AlarmService to set the alarm
 * 2a. Alarm has been set, and should now be written to the database
 * 2b. For some reason setting the alarm did not work; avoid writing to database
 * 3. Profit?
 *
 **/

class SetAlarmTest {

    val repo:IAlarmRepository = mockk()
    val service: IAlarmService = mockk()

    val set = SetAlarm(service, repo)

    @BeforeEach
    fun build() {
        clearAllMocks()
    }

    /**
     * a:
     * 1. Tell service to set an alarm
     * 2. Write that alarm to database with isActive = true
     * 3. Profit?
     */
    @Test
    fun `Alarm Set`() = runBlockingTest {
        coEvery { service.setAlarm(getAlarm(active = false)) } returns ResultWrapper.build { Unit }
        coEvery { repo.updateAlarm(getAlarm(active = true)) } returns ResultWrapper.build { Unit }

        set.execute(getAlarm(active = false))

        coVerify { service.setAlarm(getAlarm(active = false)) }
        coVerify { repo.updateAlarm(getAlarm(active = true)) }
    }

    /**
     * a:
     * 1. Tell service to set an alarm
     * 2. Definitely not Profit :(
     */
    @Test
    fun `Alarm not set`() = runBlockingTest {
        coEvery { service.setAlarm(getAlarm(active = false)) } returns ResultWrapper.build {  throw AlarmServiceException }

        set.execute(getAlarm(active = false))

        coVerify { service.setAlarm(getAlarm(active = false)) }
        //ensure this was not called:
        coVerify(exactly = 0) { repo.updateAlarm(getAlarm(active = true)) }
    }

}