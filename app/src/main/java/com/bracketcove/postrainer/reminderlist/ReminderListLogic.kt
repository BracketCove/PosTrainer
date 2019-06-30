package com.bracketcove.postrainer.reminderlist

import com.bracketcove.postrainer.BaseLogic
import com.bracketcove.postrainer.ERROR_GENERIC
import com.bracketcove.postrainer.REMINDER_CANCELLED
import com.bracketcove.postrainer.REMINDER_SET
import com.bracketcove.postrainer.dependencyinjection.AndroidReminderProvider
import com.wiseassblog.common.ResultWrapper
import com.wiseassblog.domain.domainmodel.Reminder
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * Retrieves a List of any Reminders which are Present in ReminderDatabase (Realm Database), and
 * displays passes them to the View.
 * Created by Ryan on 05/03/2017.
 */

class ReminderListLogic(
    private val view: ReminderListContract.View,
    //Service Locator
    private val provider: AndroidReminderProvider,
    dispatcher: CoroutineDispatcher
) : BaseLogic<ReminderListEvent>(dispatcher) {

    //Note: these come from BaseLogic base abstract class
    override val coroutineContext: CoroutineContext
        get() = main + jobTracker

    override fun handleEvent(eventType: ReminderListEvent) {
        when (eventType) {
            is ReminderListEvent.OnStart -> getAlarmList()
            is ReminderListEvent.OnSettingsClick -> showSettings()
            is ReminderListEvent.OnMovementsClick -> showMovements()
            is ReminderListEvent.OnCreateButtonClick -> createAlarm()
            is ReminderListEvent.OnReminderToggled -> onToggle(eventType.isActive, eventType.reminder)
            is ReminderListEvent.OnReminderIconClick -> onIconClick(eventType.reminder)
        }
    }

    private fun showMovements() {
        view.startMovementsView()
    }

    private fun onIconClick(reminder: Reminder) = launch {

        if (reminder.isActive) provider.cancelReminder.execute(reminder)
        //since there isn't much I can do if this thing fails, not bothering
        //to check the result currently

        view.startReminderDetailView(reminder.reminderId!!)
    }

    private fun onToggle(active: Boolean, reminder: Reminder) {
        if (active) setAlarm(reminder)
        else cancelAlarm(reminder)
    }

    private fun cancelAlarm(reminder: Reminder) = launch {
        val result = provider.cancelReminder.execute(reminder)

        when (result) {
            is ResultWrapper.Success -> alarmCancelled()
            is ResultWrapper.Error -> handleError()
        }
    }

    private fun alarmCancelled() {
        view.showMessage(REMINDER_CANCELLED)
    }

    private fun setAlarm(reminder: Reminder) = launch {
        val result = provider.setReminder.execute(reminder)

        when (result) {
            is ResultWrapper.Success -> alarmSet()
            is ResultWrapper.Error -> handleError()
        }
    }

    private fun alarmSet() {
        view.showMessage(REMINDER_SET)
    }

    private fun createAlarm() {
        view.startReminderDetailView("")
    }

    private fun showSettings() {
        view.startSettingsActivity()
    }

    private fun getAlarmList() = launch {
        val result = provider.getReminderList.execute()

        when (result){
            is ResultWrapper.Success -> updateView(result.value)
            is ResultWrapper.Error -> {
                view.setNoReminderListDataFound()
                handleError()
            }
        }
    }

    private fun handleError() {
            view.showMessage(ERROR_GENERIC)
    }

    private fun updateView(value: List<Reminder>) {
        if (value.size == 0) view.setNoReminderListDataFound()
        else view.setReminderListData(value)
    }
}
