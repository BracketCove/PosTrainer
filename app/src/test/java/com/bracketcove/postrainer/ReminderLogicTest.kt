package com.bracketcove.postrainer

import com.bracketcove.postrainer.dependencyinjection.AndroidReminderProvider
import com.bracketcove.postrainer.reminder.ReminderDetailContract
import com.bracketcove.postrainer.reminder.ReminderDetailEvent
import com.bracketcove.postrainer.reminder.ReminderDetailLogic
import com.wiseassblog.common.ReminderRepositoryException
import com.wiseassblog.common.ResultWrapper
import com.wiseassblog.domain.usecase.DeleteReminder
import com.wiseassblog.domain.usecase.GetReminder
import com.wiseassblog.domain.usecase.UpdateOrCreateReminder
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ReminderLogicTest {

    private val view: ReminderDetailContract.View = mockk(relaxed = true)
    private val viewModel: ReminderDetailContract.ViewModel = mockk(relaxed = true)
    private val provider: AndroidReminderProvider = mockk()

    /*
    Use Cases for this feature:
    GetReminder
    UpdateOrCreateReminder
     */
    private val getReminder: GetReminder = mockk()
    private val deleteReminder: DeleteReminder = mockk()
    private val updateReminder: UpdateOrCreateReminder = mockk()

    private val alarmDetailLogic = ReminderDetailLogic(
        view,
        viewModel,
        provider,
        Dispatchers.Unconfined
    )

    @BeforeEach
    fun setUp() {
        clearAllMocks()
        every { provider.getReminder } returns getReminder
        every { provider.updateOrCreateReminder } returns updateReminder
        every { provider.deleteReminder } returns deleteReminder

    }

    /**
     * Note: ViewModel will have a dummy reminder added to it within the Dependency Injection class, which will contain the Reminder ID which was passed in to this feature. However, the rest of the state
     * for that dummy reminder is wrong, so it will need to be updated immediately on start.
     * 1. Ask ViewModel for id
     * 2. Call getReminder(id)
     * 3. update ViewModel
     * 4. update View
     */
    @Test
    fun `On Start id valid`() {
        val ALARM = getReminder()
        every { viewModel.getReminder() } returns ALARM
        coEvery { getReminder.execute(any()) } returns ResultWrapper.build { com.bracketcove.postrainer.getReminder() }

        alarmDetailLogic.handleEvent(ReminderDetailEvent.OnStart)

        verify { viewModel.getReminder() }
        verify { viewModel.setReminder(ALARM) }

        verify { view.setPickerTime(ALARM.hourOfDay, ALARM.minute) }
        verify { view.setReminderTitle(ALARM.reminderTitle) }
        verify { view.setVibrateOnly(ALARM.isVibrateOnly) }
        verify { view.setRenewAutomatically(ALARM.isRenewAutomatically) }

        coVerify { getReminder.execute(any()) }
    }

    /**
     * Blank id indicates that the user wants to create a new reminder.
     * 1. Retrieve reminder from VM
     * 2. Write reminder back to VM with newly generated id
     */
    @Test
    fun `On Start id blank`() {
        val ALARM = getReminder(id = "")
        every { viewModel.getReminder() } returns ALARM

        alarmDetailLogic.handleEvent(ReminderDetailEvent.OnStart)

        verify { viewModel.getReminder() }
        verify { viewModel.setReminder(any()) }

        verify { view.setPickerTime(ALARM.hourOfDay, ALARM.minute) }
        verify { view.setReminderTitle(ALARM.reminderTitle) }
        verify { view.setVibrateOnly(ALARM.isVibrateOnly) }
        verify { view.setRenewAutomatically(ALARM.isRenewAutomatically) }

        coVerify(exactly = 0) { getReminder.execute(any()) }
    }

    @Test
    fun `On Start id invalid`() {
        val ALARM = getReminder(id = null)
        every { viewModel.getReminder() } returns ALARM

        alarmDetailLogic.handleEvent(ReminderDetailEvent.OnStart)

        verify(exactly = 1) { viewModel.getReminder() }
        verify(exactly = 1) { view.startReminderListActivity() }
        verify(exactly = 1) { view.showMessage(ERROR_GENERIC) }
    }

    /**
     * On done: User is ready to add/update the current reminder and return to Reminder List
     * 1. Grab current state of the View (includes Title, picker time, vibrateOnly, renewAutomatically)
     * 2. Create new copy from View State + proper ID from ViewModel
     * 3. Give that copy to updateOrCreateReminder use case: Success
     * 4. StartList Activity
     */
    @Test
    fun `On Done Update Successful`() {
        val ALARM = getReminder()

        every { view.getState() } returns ALARM
        every { viewModel.getReminder() } returns ALARM
        coEvery { updateReminder.execute(ALARM) } returns ResultWrapper.build { Unit }

        alarmDetailLogic.handleEvent(ReminderDetailEvent.OnDoneIconPress)

        verify(exactly = 1) { viewModel.getReminder() }
        verify(exactly = 1) { view.getState() }
        verify(exactly = 1) { view.showMessage(REMINDER_UPDATED) }
        verify(exactly = 1) { view.startReminderListActivity() }
        coVerify { updateReminder.execute(ALARM) }
    }

    @Test
    fun `On Done Update unsuccessful`() {
        val ALARM = getReminder()

        every { view.getState() } returns ALARM
        every { viewModel.getReminder() } returns ALARM
        coEvery { updateReminder.execute(ALARM) } returns ResultWrapper.build { throw ReminderRepositoryException }

        alarmDetailLogic.handleEvent(ReminderDetailEvent.OnDoneIconPress)

        verify(exactly = 1) { viewModel.getReminder() }
        verify(exactly = 1) { view.getState() }
        verify(exactly = 1) { view.showMessage(ERROR_GENERIC) }
        verify(exactly = 1) { view.startReminderListActivity() }
        coVerify { updateReminder.execute(ALARM) }
    }

    @Test
    fun `On Done empty reminder name`() {
        val ALARM = getReminder(id = "")

        every { view.getState() } returns ALARM
        every { viewModel.getReminder() } returns ALARM


        alarmDetailLogic.handleEvent(ReminderDetailEvent.OnDoneIconPress)

        verify(exactly = 1) { viewModel.getReminder() }
        verify(exactly = 1) { view.getState() }
        verify(exactly = 1) { view.showMessage(PROMPT_ENTER_NAME) }
        coVerify { updateReminder.execute(ALARM) }
    }

    /**
     *On back: User wishes to return to List View without updates
     * 1. Tell view to navigate
    */
    @Test
    fun `On Back`() {

        alarmDetailLogic.handleEvent(ReminderDetailEvent.OnBackIconPress)

        verify(exactly = 1) { view.startReminderListActivity() }
    }

    /**
     *On Delete: User probably wants to delete the reminder
     * 1. Ask user for confirmation
     */
    @Test
    fun `On Delete Icon Press`() {

        alarmDetailLogic.handleEvent(ReminderDetailEvent.OnDeleteIconPress)

        verify(exactly = 1) { view.showDeleteConfirm() }
    }

    /**
     *On Delete confirmed: User definitely wants to delete the reminder
     * 1. Ask viewModel for id
     * 2. Give id to deleteReminder use case
     * 3. start list activity
     */
    @Test
    fun `On Delete confirmed`() {
        val ALARM = getReminder()

        every { viewModel.getReminder() } returns ALARM
        coEvery{ deleteReminder.execute(ALARM)} returns ResultWrapper.build { Unit }

        alarmDetailLogic.handleEvent(ReminderDetailEvent.OnDeleteConfirmed)

        verify(exactly = 1) { view.startReminderListActivity() }
        verify(exactly = 1) { view.showMessage(REMINDER_DELETED) }
        coVerify(exactly = 1) { deleteReminder.execute(ALARM) }
    }
}
