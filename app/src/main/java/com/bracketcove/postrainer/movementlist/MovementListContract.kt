package com.bracketcove.postrainer.movementlist

import com.wiseassblog.domain.domainmodel.Reminder

interface MovementListContract {

    interface View {
        fun setReminderListData(reminderListData: List<Reminder>)

        fun startReminderDetailView(reminderId: String)

        fun startSettingsActivity()

        fun showMessage(msg: String)

        fun startRemindersView()
    }
}

sealed class MovementListEvent {
    object OnSettingsClick : MovementListEvent()
    object OnMovementsClick : MovementListEvent()
    object OnCreateButtonClick : MovementListEvent()
    data class OnMovementIconClick(val reminder: Reminder) : MovementListEvent()
    data class OnMovementToggled(val isActive: Boolean, val reminder: Reminder) : MovementListEvent()
    object OnStart : MovementListEvent()
}