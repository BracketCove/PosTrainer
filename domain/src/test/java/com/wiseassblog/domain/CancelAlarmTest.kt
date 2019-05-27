package com.wiseassblog.domain

import com.wiseassblog.common.ResultWrapper
import com.wiseassblog.domain.repository.IAlarmRepository
import com.wiseassblog.domain.repository.IAlarmService
import com.wiseassblog.domain.usecase.CancelAlarm
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CancelAlarmTest {

    val service: IAlarmService = mockk()
    val repo: IAlarmRepository = mockk()

    val cancel = CancelAlarm(service, repo)

    @BeforeEach
    fun build(){
        clearAllMocks()
    }

    /**
     * 1. Tell alarmService to cancel the alarm
     * 2. Write alarm to database, with active = false
     */
    @Test
    fun `On Alarm Cancelled`() = runBlockingTest {
        coEvery { service.cancelAlarm(getAlarm()) } returns ResultWrapper.build { Unit }
        coEvery { repo.updateAlarm(getAlarm(active = false)) } returns ResultWrapper.build { Unit }

        cancel.execute(getAlarm())

        coVerify { service.cancelAlarm(getAlarm()) }
        coVerify { repo.updateAlarm(getAlarm(active = false)) }
    }
}