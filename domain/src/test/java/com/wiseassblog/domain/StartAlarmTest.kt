package com.wiseassblog.domain

import com.wiseassblog.common.ResultWrapper
import com.wiseassblog.domain.repository.IAlarmRepository
import com.wiseassblog.domain.repository.IAlarmService
import com.wiseassblog.domain.usecase.StartAlarm
import io.mockk.*
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

/**
 *Start Alarm:
 * 1. Retrieve Alarm from database
 * 2. Give Alarm to service
 * Created by R_KAY on 5/23/2017.
 */
class StartAlarmTest {
    private val service: IAlarmService = mockk()
    private val repo: IAlarmRepository = mockk()

    private val start = StartAlarm(service, repo)

    @BeforeEach
    fun build() {
        clearAllMocks()
    }

    /**
     *Start Alarm:
     * 1. Retrieve Alarm from database
     * 2. Give Alarm to service
     * Created by R_KAY on 5/23/2017.
     */
    @Test
    fun `Alarm Start`() = runBlockingTest {

        coEvery { repo.getAlarmById(getAlarm().alarmId!!) } returns ResultWrapper.build { getAlarm() }
        coEvery { service.startAlarm(getAlarm()) } returns ResultWrapper.build { Unit }

        start.execute(getAlarm().alarmId!!)

        coVerify { repo.getAlarmById(getAlarm().alarmId!!) }
        coVerify { service.startAlarm(getAlarm()) }
    }


    @AfterEach
    fun confirm() {
        confirmVerified(service, repo)
    }

}