package com.bracketcove.postrainer

import com.bracketcove.postrainer.alarm.AlarmDetailContract
import com.bracketcove.postrainer.alarm.AlarmDetailEvent
import com.bracketcove.postrainer.alarm.AlarmDetailLogic
import com.bracketcove.postrainer.dependencyinjection.AndroidDependencyProvider
import com.wiseassblog.common.AlarmRepositoryException
import com.wiseassblog.common.ResultWrapper
import com.wiseassblog.domain.usecase.DeleteAlarm
import com.wiseassblog.domain.usecase.GetAlarm
import com.wiseassblog.domain.usecase.UpdateOrCreateAlarm
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AlarmLogicTest {

    private val view: AlarmDetailContract.View = mockk(relaxed = true)
    private val viewModel: AlarmDetailContract.ViewModel = mockk(relaxed = true)
    private val provider: AndroidDependencyProvider = mockk()
    private val dispatcher: Dispatchers = mockk()

    /*
    Use Cases for this feature:
    GetAlarm
    UpdateOrCreateAlarm
     */
    private val getAlarm: GetAlarm = mockk()
    private val deleteAlarm: DeleteAlarm = mockk()
    private val updateAlarm: UpdateOrCreateAlarm = mockk()

    private val alarmDetailLogic = AlarmDetailLogic(
        view,
        viewModel,
        provider,
        Dispatchers.Unconfined
    )

    @BeforeEach
    fun setUp() {
        clearAllMocks()
        every { provider.getAlarm } returns getAlarm
        every { provider.updateOrCreateAlarm } returns updateAlarm
        every { provider.deleteAlarm } returns deleteAlarm

    }

    /**
     * Note: ViewModel will have a dummy alarm added to it within the Dependency Injection class, which will contain the Alarm ID which was passed in to this feature. However, the rest of the state
     * for that dummy alarm is wrong, so it will need to be updated immediately on start.
     * 1. Ask ViewModel for id
     * 2. Call getAlarm(id)
     * 3. update ViewModel
     * 4. update View
     */
    @Test
    fun `On Start id valid`() {
        val ALARM = getAlarm()
        every { viewModel.getAlarm() } returns ALARM
        coEvery { getAlarm.execute(any()) } returns ResultWrapper.build { com.bracketcove.postrainer.getAlarm() }

        alarmDetailLogic.handleEvent(AlarmDetailEvent.OnStart)

        verify { viewModel.getAlarm() }
        verify { viewModel.setAlarm(ALARM) }

        verify { view.setPickerTime(ALARM.hourOfDay, ALARM.minute) }
        verify { view.setAlarmTitle(ALARM.alarmTitle) }
        verify { view.setVibrateOnly(ALARM.isVibrateOnly) }
        verify { view.setRenewAutomatically(ALARM.isRenewAutomatically) }

        coVerify { getAlarm.execute(any()) }
    }

    /**
     * Blank id indicates that the user wants to create a new alarm.
     * 1. Retrieve alarm from VM
     * 2. Write alarm back to VM with newly generated id
     */
    @Test
    fun `On Start id blank`() {
        val ALARM = getAlarm(id = "")
        every { viewModel.getAlarm() } returns ALARM

        alarmDetailLogic.handleEvent(AlarmDetailEvent.OnStart)

        verify { viewModel.getAlarm() }
        verify { viewModel.setAlarm(any()) }

        verify { view.setPickerTime(ALARM.hourOfDay, ALARM.minute) }
        verify { view.setAlarmTitle(ALARM.alarmTitle) }
        verify { view.setVibrateOnly(ALARM.isVibrateOnly) }
        verify { view.setRenewAutomatically(ALARM.isRenewAutomatically) }

        coVerify(exactly = 0) { getAlarm.execute(any()) }
    }

    @Test
    fun `On Start id invalid`() {
        val ALARM = getAlarm(id = null)
        every { viewModel.getAlarm() } returns ALARM

        alarmDetailLogic.handleEvent(AlarmDetailEvent.OnStart)

        verify(exactly = 1) { viewModel.getAlarm() }
        verify(exactly = 1) { view.startAlarmListActivity() }
        verify(exactly = 1) { view.showMessage(ERROR_GENERIC) }
    }

    /**
     * On done: User is ready to add/update the current alarm and return to Alarm List
     * 1. Grab current state of the View (includes Title, picker time, vibrateOnly, renewAutomatically)
     * 2. Create new copy from View State + proper ID from ViewModel
     * 3. Give that copy to updateOrCreateAlarm use case: Success
     * 4. StartList Activity
     */
    @Test
    fun `On Done Update Successful`() {
        val ALARM = getAlarm()

        every { view.getState() } returns ALARM
        every { viewModel.getAlarm() } returns ALARM
        coEvery { updateAlarm.execute(ALARM) } returns ResultWrapper.build { Unit }

        alarmDetailLogic.handleEvent(AlarmDetailEvent.OnDoneIconPress)

        verify(exactly = 1) { viewModel.getAlarm() }
        verify(exactly = 1) { view.getState() }
        verify(exactly = 1) { view.showMessage(ALARM_UPDATED) }
        verify(exactly = 1) { view.startAlarmListActivity() }
        coVerify { updateAlarm.execute(ALARM) }
    }

    @Test
    fun `On Done Update unsuccessful`() {
        val ALARM = getAlarm()

        every { view.getState() } returns ALARM
        every { viewModel.getAlarm() } returns ALARM
        coEvery { updateAlarm.execute(ALARM) } returns ResultWrapper.build { throw AlarmRepositoryException }

        alarmDetailLogic.handleEvent(AlarmDetailEvent.OnDoneIconPress)

        verify(exactly = 1) { viewModel.getAlarm() }
        verify(exactly = 1) { view.getState() }
        verify(exactly = 1) { view.showMessage(ERROR_GENERIC) }
        verify(exactly = 1) { view.startAlarmListActivity() }
        coVerify { updateAlarm.execute(ALARM) }
    }

    /**
     *On back: User wishes to return to List View without updates
     * 1. Tell view to navigate
    */
    @Test
    fun `On Back`() {

        alarmDetailLogic.handleEvent(AlarmDetailEvent.OnBackIconPress)

        verify(exactly = 1) { view.startAlarmListActivity() }
    }

    /**
     *On Delete: User probably wants to delete the alarm
     * 1. Ask user for confirmation
     */
    @Test
    fun `On Delete Icon Press`() {

        alarmDetailLogic.handleEvent(AlarmDetailEvent.OnDeleteIconPress)

        verify(exactly = 1) { view.showDeleteConfirm() }
    }

    /**
     *On Delete confirmed: User definitely wants to delete the alarm
     * 1. Ask viewModel for id
     * 2. Give id to deleteAlarm use case
     * 3. start list activity
     */
    @Test
    fun `On Delete confirmed`() {
        val ALARM = getAlarm()

        every { viewModel.getAlarm() } returns ALARM
        coEvery{ deleteAlarm.execute(ALARM)} returns ResultWrapper.build { Unit }

        alarmDetailLogic.handleEvent(AlarmDetailEvent.OnDeleteConfirmed)

        verify(exactly = 1) { view.startAlarmListActivity() }
        verify(exactly = 1) { view.showMessage(ALARM_DELETED) }
        coVerify(exactly = 1) { deleteAlarm.execute(ALARM) }
    }
}
