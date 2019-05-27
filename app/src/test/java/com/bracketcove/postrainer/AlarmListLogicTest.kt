package com.bracketcove.postrainer

import com.bracketcove.postrainer.alarmlist.AlarmListContract
import com.bracketcove.postrainer.alarmlist.AlarmListEvent
import com.bracketcove.postrainer.alarmlist.AlarmListLogic
import com.bracketcove.postrainer.dependencyinjection.AndroidDependencyProvider
import com.wiseassblog.common.AlarmRepositoryException
import com.wiseassblog.common.ResultWrapper
import com.wiseassblog.domain.domainmodel.Alarm
import com.wiseassblog.domain.usecase.CancelAlarm
import com.wiseassblog.domain.usecase.GetAlarmList
import com.wiseassblog.domain.usecase.SetAlarm
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

/**
 * Created by Ryan on 09/03/2017.
 */
class AlarmListLogicTest {

    private val view: AlarmListContract.View = mockk(relaxed = true)
    private val provider: AndroidDependencyProvider = mockk()

    /*
    Use Cases for this feature:
    GetAlarm
    UpdateOrCreateAlarm
     */
    private val getAlarmList: GetAlarmList = mockk()
    private val setAlarm: SetAlarm = mockk()
    private val cancelAlarm: CancelAlarm = mockk()

    private val alarmListLogic = AlarmListLogic(
        view,
        provider,
        Dispatchers.Unconfined
    )

    @BeforeEach
    fun setUp() {
        clearAllMocks()
        every { provider.getAlarmList } returns getAlarmList
        every { provider.setAlarm } returns setAlarm
        every { provider.cancelAlarm } returns cancelAlarm
    }

    /**
     *Get Alarms: When Alarm List feature's onStart is called, get current alarms
     * 1. Request list of alarms from Use Case
     * 2. Update view
     */
    @Test
    fun `On Start get alarms`() {
        val ALARM_LIST: List<Alarm> = listOf(getAlarm(), getAlarm())

        coEvery { getAlarmList.execute() } returns ResultWrapper.build { ALARM_LIST }

        alarmListLogic.handleEvent(AlarmListEvent.OnStart)

        verify(exactly = 1) { view.setAlarmListData(ALARM_LIST) }
        coVerify { getAlarmList.execute() }
    }

    @Test
    fun `On Start get alarms empty`() {
        val ALARM_LIST: List<Alarm> = emptyList()

        coEvery { getAlarmList.execute() } returns ResultWrapper.build { ALARM_LIST }

        alarmListLogic.handleEvent(AlarmListEvent.OnStart)

        verify(exactly = 1) { view.setNoAlarmListDataFound() }
        coVerify { getAlarmList.execute() }
    }

    @Test
    fun `On Start get alarms error`() {
        coEvery { getAlarmList.execute() } returns ResultWrapper.build { throw AlarmRepositoryException }

        alarmListLogic.handleEvent(AlarmListEvent.OnStart)

        verify(exactly = 1) { view.showMessage(ERROR_GENERIC) }
        verify(exactly = 1) { view.setNoAlarmListDataFound() }
        coVerify { getAlarmList.execute() }
    }

    /**
     *User wants to look at settings
     * 1. tell view to navigate to settings
     */
    @Test
    fun `On Settings Click`() {

        alarmListLogic.handleEvent(AlarmListEvent.OnSettingsClick)

        verify(exactly = 1) { view.startSettingsActivity() }
    }

    /**
     * Create Alarm: User wants to create a new Alarm
     * 1. Create a new Alarm with generic values except for the alarmId (dynamically generated)
     * 2. Tell view to start detail activity with Id as an extra
     */
    @Test
    fun `On Create Alarm click`() {
        alarmListLogic.handleEvent(AlarmListEvent.OnCreateButtonClick)

        verify(exactly = 1) { view.startAlarmDetailActivity("") }
    }

    /**
     * On Alarm Set: User wants to make an Alarm active
     * 1. Check boolean active from adapter (forwarded by the View). This boolean is the "intended" state of the alarm after the update is called, since it is based on the Switch state in the View
     * 2. Call appropriate Use Case based on boolean
     * 3. Show message if successful or not
     */
    @Test
    fun `On Toggle Alarm set new alarm`() {
        val ALARM = getAlarm(active = false)
        val ACTIVE = true

        coEvery { setAlarm.execute(ALARM) } returns ResultWrapper.build { Unit }

        alarmListLogic.handleEvent(AlarmListEvent.OnAlarmToggled(ACTIVE, ALARM))

        coVerify(exactly = 1) { setAlarm.execute(ALARM) }
        verify(exactly = 1) { view.showMessage(ALARM_SET) }
    }

    @Test
    fun `On Toggle Alarm cancel active alarm`() {
        val ALARM = getAlarm(active = true)
        val ACTIVE = false

        coEvery { cancelAlarm.execute(ALARM) } returns ResultWrapper.build { Unit }

        alarmListLogic.handleEvent(AlarmListEvent.OnAlarmToggled(ACTIVE, ALARM))

        coVerify(exactly = 1) { cancelAlarm.execute(ALARM) }
        verify(exactly = 1) { view.showMessage(ALARM_CANCELLED) }
    }

    /**
     * On Alarm Clicked: User wants to edit the details of a particular alarm.
     * 1. If alarm is currently active, in order to avoid problems with shared mutable state, deactivate the alarm
     * before proceeding to the detail screen
     * 2. proceed to the detail screen
     */
    @Test
    fun `On Alarm Icon clicked active`() {
        val ALARM = getAlarm(active = true)

        coEvery { cancelAlarm.execute(ALARM) } returns ResultWrapper.build { Unit }

        alarmListLogic.handleEvent(AlarmListEvent.OnAlarmIconClick(ALARM))

        coVerify(exactly = 1) { cancelAlarm.execute(ALARM) }
        verify(exactly = 1) { view.startAlarmDetailActivity(ALARM.alarmId!!) }
    }

    @Test
    fun `On Alarm Icon clicked inactive`() {
        val ALARM = getAlarm(active = false)

        alarmListLogic.handleEvent(AlarmListEvent.OnAlarmIconClick(ALARM))

        coVerify(exactly = 0) { cancelAlarm.execute(ALARM) }
        verify(exactly = 1) { view.startAlarmDetailActivity(ALARM.alarmId!!) }
    }

}