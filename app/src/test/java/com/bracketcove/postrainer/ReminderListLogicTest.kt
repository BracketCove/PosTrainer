package com.bracketcove.postrainer

import com.bracketcove.postrainer.dependencyinjection.AndroidReminderProvider
import com.bracketcove.postrainer.reminderlist.ReminderListContract
import com.bracketcove.postrainer.reminderlist.ReminderListEvent
import com.bracketcove.postrainer.reminderlist.ReminderListLogic
import com.wiseassblog.common.ReminderRepositoryException
import com.wiseassblog.common.ResultWrapper
import com.wiseassblog.domain.domainmodel.Reminder
import com.wiseassblog.domain.usecase.CancelReminder
import com.wiseassblog.domain.usecase.GetReminderList
import com.wiseassblog.domain.usecase.SetReminder
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

/**
 * Created by Ryan on 09/03/2017.
 */
class ReminderListLogicTest {

    private val view: ReminderListContract.View = mockk(relaxed = true)
    private val provider: AndroidReminderProvider = mockk()

    /*
    Use Cases for this feature:
    GetReminder
    UpdateOrCreateReminder
     */
    private val getReminderList: GetReminderList = mockk()
    private val setReminder: SetReminder = mockk()
    private val cancelReminder: CancelReminder = mockk()

    private val reminderListLogic = ReminderListLogic(
        view,
        provider,
        Dispatchers.Unconfined
    )

    @BeforeEach
    fun setUp() {
        clearAllMocks()
        every { provider.getReminderList } returns getReminderList
        every { provider.setReminder } returns setReminder
        every { provider.cancelReminder } returns cancelReminder
    }

    /**
     *Get Reminders: When Reminder List feature's onStart is called, get current alarms
     * 1. Request list of Reminders from Use Case
     * 2. Update view
     */
    @Test
    fun `On Start get Reminders`() {
        val REMINDER_LIST: List<Reminder> = listOf(getReminder(), getReminder())

        coEvery { getReminderList.execute() } returns ResultWrapper.build { REMINDER_LIST }

        reminderListLogic.handleEvent(ReminderListEvent.OnStart)

        verify(exactly = 1) { view.setReminderListData(REMINDER_LIST) }
        coVerify { getReminderList.execute() }
    }

    @Test
    fun `On Start get Reminders empty`() {
        val REMINDER_LIST: List<Reminder> = emptyList()

        coEvery { getReminderList.execute() } returns ResultWrapper.build { REMINDER_LIST }

        reminderListLogic.handleEvent(ReminderListEvent.OnStart)

        verify(exactly = 1) { view.setNoReminderListDataFound() }
        coVerify { getReminderList.execute() }
    }

    @Test
    fun `On Start get Reminders error`() {
        coEvery { getReminderList.execute() } returns ResultWrapper.build { throw ReminderRepositoryException }

        reminderListLogic.handleEvent(ReminderListEvent.OnStart)

        verify(exactly = 1) { view.showMessage(ERROR_GENERIC) }
        verify(exactly = 1) { view.setNoReminderListDataFound() }
        coVerify { getReminderList.execute() }
    }

    /**
     *User wants to look at settings
     * 1. tell view to navigate to settings
     */
    @Test
    fun `On Settings Click`() {

        reminderListLogic.handleEvent(ReminderListEvent.OnSettingsClick)

        verify(exactly = 1) { view.startSettingsActivity() }
    }

    /**
     *User wants to look at Movements
     * 1. tell view to navigate to movements
     */
    @Test
    fun `On Movements Click`() {

        reminderListLogic.handleEvent(ReminderListEvent.OnMovementsClick)

        verify(exactly = 1) { view.startMovementsView() }
    }

    /**
     * Create Reminder: User wants to create a new Reminder
     * 1. Create a new Reminder with generic values except for the reminderId (dynamically generated)
     * 2. Tell view to start detail activity with Id as an extra
     */
    @Test
    fun `On Create Alarm click`() {
        reminderListLogic.handleEvent(ReminderListEvent.OnCreateButtonClick)

        verify(exactly = 1) { view.startReminderDetailView("") }
    }


    /**
     * On Reminder Set: User wants to make an Reminder active
     * 1. Check boolean active from adapter (forwarded by the View). This boolean is the "intended" state of the reminder after the update is called, since it is based on the Switch state in the View
     * 2. Call appropriate Use Case based on boolean
     * 3. Show message if successful or not
     */
    @Test
    fun `On Toggle Reminder set new Reminder`() {
        val REMINDER = getReminder(active = false)
        val ACTIVE = true

        coEvery { setReminder.execute(REMINDER) } returns ResultWrapper.build { Unit }

        reminderListLogic.handleEvent(ReminderListEvent.OnReminderToggled(ACTIVE, REMINDER))

        coVerify(exactly = 1) { setReminder.execute(REMINDER) }
        verify(exactly = 1) { view.showMessage(REMINDER_SET) }
    }

    @Test
    fun `On Toggle Reminder cancel active Reminder`() {
        val ALARM = getReminder(active = true)
        val ACTIVE = false

        coEvery { cancelReminder.execute(ALARM) } returns ResultWrapper.build { Unit }

        reminderListLogic.handleEvent(ReminderListEvent.OnReminderToggled(ACTIVE, ALARM))

        coVerify(exactly = 1) { cancelReminder.execute(ALARM) }
        verify(exactly = 1) { view.showMessage(REMINDER_CANCELLED) }
    }

    /**
     * On Reminder Clicked: User wants to edit the details of a particular reminder.
     * 1. If reminder is currently active, in order to avoid problems with shared mutable state, deactivate the reminder
     * before proceeding to the detail screen
     * 2. proceed to the detail screen
     */
    @Test
    fun `On Reminder Icon clicked active`() {
        val REMINDER = getReminder(active = true)

        coEvery { cancelReminder.execute(REMINDER) } returns ResultWrapper.build { Unit }

        reminderListLogic.handleEvent(ReminderListEvent.OnReminderIconClick(REMINDER))

        coVerify(exactly = 1) { cancelReminder.execute(REMINDER) }
        verify(exactly = 1) { view.startReminderDetailView(REMINDER.reminderId!!) }
    }

    @Test
    fun `On Reminder Icon clicked inactive`() {
        val REMINDER = getReminder(active = false)

        reminderListLogic.handleEvent(ReminderListEvent.OnReminderIconClick(REMINDER))

        coVerify(exactly = 0) { cancelReminder.execute(REMINDER) }
        verify(exactly = 1) { view.startReminderDetailView(REMINDER.reminderId!!) }
    }

}