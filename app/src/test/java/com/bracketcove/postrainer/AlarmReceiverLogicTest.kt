package com.bracketcove.postrainer

import com.bracketcove.postrainer.alarmreceiver.AlarmReceiverContract
import com.bracketcove.postrainer.alarmreceiver.AlarmReceiverEvent
import com.bracketcove.postrainer.alarmreceiver.AlarmReceiverLogic
import com.bracketcove.postrainer.dependencyinjection.AndroidDependencyProvider
import com.wiseassblog.common.ResultWrapper
import com.wiseassblog.domain.usecase.DismissAlarm
import com.wiseassblog.domain.usecase.StartAlarm
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AlarmReceiverLogicTest {

    private val view: AlarmReceiverContract.View = mockk(relaxed = true)
    private val provider: AndroidDependencyProvider = mockk()

    private val dismissAlarm: DismissAlarm = mockk()
    private val startAlarm: StartAlarm = mockk()

    private val logic = AlarmReceiverLogic(
        view, provider, Dispatchers.Unconfined
    )

    @BeforeEach
    fun setUp() {
        clearAllMocks()
        every { provider.dismissAlarm } returns dismissAlarm
        every { provider.startAlarm } returns startAlarm
    }

    /**
     * On Start: Pending Intent has started AlarmReceiver Feature, and it's time to make some noise
     * 1. Tell logic that it is time to rock and roll, and provide id of alarm from extras
     * 2. Give Alarm Id to startAlarm use case
     */
    @Test
    fun `On Start`() {
        coEvery { startAlarm.execute(getAlarm().alarmId!!) } returns ResultWrapper.build { Unit }

        logic.handleEvent(AlarmReceiverEvent.OnStart(getAlarm().alarmId!!))

        coVerify { startAlarm.execute(getAlarm().alarmId!!) }
    }

    /**
     * On Dismiss: User has pushed the stop Alarm button
     * 1. Tell Use case to stop the alarms
     * 2. Tell View to finish
     */
    @Test
    fun `On Dismiss`() {
        coEvery { dismissAlarm.execute(getAlarm().alarmId!!) } returns ResultWrapper.build { Unit }

        logic.handleEvent(AlarmReceiverEvent.OnDismissed(getAlarm().alarmId!!))

        coVerify { dismissAlarm.execute(getAlarm().alarmId!!) }
        coVerify { view.finish() }
    }
}