package com.bracketcove.postrainer.reminder


import com.bracketcove.postrainer.BaseLogic
import com.bracketcove.postrainer.ERROR_GENERIC
import com.bracketcove.postrainer.REMINDER_DELETED
import com.bracketcove.postrainer.REMINDER_UPDATED
import com.bracketcove.postrainer.dependencyinjection.AndroidReminderProvider
import com.wiseassblog.common.ResultWrapper
import com.wiseassblog.common.getFormattedDate
import com.wiseassblog.domain.domainmodel.Reminder
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ReminderDetailLogic(
    private val view: ReminderDetailContract.View,
    private val viewModel: ReminderDetailContract.ViewModel,
    private val provider: AndroidReminderProvider,
    dispatcher: CoroutineDispatcher
) : BaseLogic<ReminderDetailEvent>(dispatcher) {

    //Note: these come from BaseLogic base abstract class
    override val coroutineContext: CoroutineContext
        get() = main + jobTracker

    override fun handleEvent(eventType: ReminderDetailEvent) {
        when (eventType) {
            is ReminderDetailEvent.OnStart -> onStart()
            is ReminderDetailEvent.OnDoneIconPress -> onDone()
            is ReminderDetailEvent.OnBackIconPress -> onBack()
            is ReminderDetailEvent.OnDeleteIconPress -> onDelete()
            is ReminderDetailEvent.OnDeleteConfirmed -> onDeleteConfirmed()
        }
    }

    private fun onDeleteConfirmed() = launch {
        val result = provider.deleteReminder.execute(
            viewModel.getReminder()!!
        )

        when (result) {
            is ResultWrapper.Success -> onDeleted()
        }

    }

    private fun onDeleted() {
        view.showMessage(REMINDER_DELETED)
        view.startReminderListActivity()
    }

    private fun onDelete() {
        view.showDeleteConfirm()
    }

    private fun onBack() {
        view.startReminderListActivity()
    }

    private fun onDone() = launch {
        val alarmUpdate = view.getState().copy(
            reminderId = viewModel.getReminder()!!.reminderId
        )

        val result = provider.updateOrCreateReminder.execute(alarmUpdate)

        when (result) {
            is ResultWrapper.Success -> onUpdate()
            is ResultWrapper.Error -> handleError()
        }
    }

    private fun onUpdate() {
        view.showMessage(REMINDER_UPDATED)
        view.startReminderListActivity()
    }

    private fun onStart() = launch {
        val alarmId = viewModel.getReminder()?.reminderId
        if (alarmId != null && alarmId != "") {
            val result = provider.getReminder.execute(alarmId)

            when (result) {
                is ResultWrapper.Success -> updateView(result.value)
                is ResultWrapper.Error -> handleError()
            }
        } else if (alarmId == "") {
            viewModel.setReminder(
                viewModel.getReminder()!!
                    .copy(reminderId = getFormattedDate())
                    .also {
                        updateView(it)
                    }
            )

        } else {
            handleError()
        }
    }

    private fun handleError() {
        view.showMessage(ERROR_GENERIC)
        view.startReminderListActivity()
    }

    private fun updateView(reminder: Reminder) {
        viewModel.setReminder(reminder)
        view.setReminderTitle(reminder.reminderTitle)
        view.setPickerTime(reminder.hourOfDay, reminder.minute)
        view.setVibrateOnly(reminder.isVibrateOnly)
        view.setRenewAutomatically(reminder.isRenewAutomatically)
    }

}
