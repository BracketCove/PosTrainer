package com.bracketcove.postrainer.reminderlist

import com.wiseassblog.domain.domainmodel.Reminder

/**
 * Created by Ryan on 06/03/2017.
 */

interface ReminderListContract {
    interface View {
        fun setReminderListData(reminderListData: List<Reminder>)

        fun setNoReminderListDataFound()

        fun startReminderDetailView(reminderId: String)

        fun startSettingsActivity()

        fun showMessage(msg: String)

        fun startMovementsView()
    }
}

sealed class ReminderListEvent {
    object OnSettingsClick : ReminderListEvent()
    object OnMovementsClick : ReminderListEvent()
    object OnCreateButtonClick : ReminderListEvent()
    data class OnReminderIconClick(val reminder: Reminder) : ReminderListEvent()
    data class OnReminderToggled(val isActive: Boolean, val reminder: Reminder) : ReminderListEvent()
    object OnStart : ReminderListEvent()
}