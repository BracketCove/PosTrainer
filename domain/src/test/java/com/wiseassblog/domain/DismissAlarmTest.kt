package com.wiseassblog.domain

import com.wiseassblog.common.AlarmRepositoryException
import com.wiseassblog.common.ResultWrapper
import com.wiseassblog.domain.repository.IAlarmRepository
import com.wiseassblog.domain.repository.IAlarmService
import com.wiseassblog.domain.usecase.DismissAlarm
import io.mockk.*
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

/**
 *Dismiss Alarm:
 * 1. Tell system services (MediaPlayer, Vibrator, etc.) to STFU
 * 2. Retrieve alarm from database
 * 3a. Alarm alarm is rewritten to database inactive
 * 3b. Alarm is reset for 24hr ahead in time
 * 4b. Alarm is rewritten to database active
 * Created by R_KAY on 5/23/2017.
 */
class DismissAlarmTest {
    private val service: IAlarmService = mockk()
    private val repo: IAlarmRepository = mockk()

    private val dismiss = DismissAlarm(service, repo)

    @BeforeEach
    fun build() {
        clearAllMocks()
    }

    /**
     * a:
     * 1. Tell service to stop alarm sounds
     * 2. Retrieve alarm from Database
     * 3. Check Alarm Status: renewAutomatically = false
     * 4. Write alarm to database with active = false
     */
    @Test
    fun `Alarm Dismissed no renew`() = runBlockingTest {
        coEvery { service.dismissAlarm() } returns ResultWrapper.build { Unit }
        coEvery { repo.getAlarmById(getAlarm().alarmId!!) } returns ResultWrapper.build { getAlarm() }
        coEvery { repo.updateAlarm(getAlarm(active = false)) } returns ResultWrapper.build { Unit }

        dismiss.execute(getAlarm().alarmId!!)

        coVerify { service.dismissAlarm() }
        coVerify { repo.getAlarmById(getAlarm().alarmId!!) }
        coVerify { repo.updateAlarm(getAlarm(active = false)) }
    }

    /**
     * b:
     * 1. Tell service to stop alarm sounds
     * 2. Retrieve alarm from Database
     * 3. Check Alarm Status: renewAutomatically = true
     */
    @Test
    fun `Alarm Dismissed renewed`() = runBlockingTest {
        coEvery { service.dismissAlarm() } returns ResultWrapper.build { Unit }
        coEvery { repo.getAlarmById(getAlarm().alarmId!!) } returns ResultWrapper.build { getAlarm(renewAutomatically = true) }

        dismiss.execute(getAlarm().alarmId!!)

        coVerify { service.dismissAlarm() }
        coVerify { repo.getAlarmById(getAlarm().alarmId!!) }
    }

    /**
     * c:
     * 1. Tell service to stop alarm sounds
     * 2. Retrieve alarm from Database = throw AlarmRepositoryException
     */
    @Test
    fun `Alarm Dismissed Id Invalid`() = runBlockingTest {
        coEvery { service.dismissAlarm() } returns ResultWrapper.build { Unit }
        coEvery { repo.getAlarmById(getAlarm().alarmId!!) } returns ResultWrapper.build { throw AlarmRepositoryException }

        val dismissResult = dismiss.execute(getAlarm().alarmId!!)

        coVerify { service.dismissAlarm() }
        coVerify { repo.getAlarmById(getAlarm().alarmId!!) }

        assert(dismissResult is ResultWrapper.Error)
    }

    @AfterEach
    fun confirm() {
        confirmVerified(service, repo)
    }

}