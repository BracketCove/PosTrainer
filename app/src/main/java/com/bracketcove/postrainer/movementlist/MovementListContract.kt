package com.bracketcove.postrainer.movementlist

import com.wiseassblog.domain.domainmodel.Movement

interface MovementListContract {

    interface View {
        fun setMovementListData(movementListData: List<Movement>)

        fun startMovementView(movementId: String)

        fun startSettingsView()

        fun showMessage(msg: String)

        fun startRemindersView()
    }
}

sealed class MovementListEvent {
    object OnSettingsClick : MovementListEvent()
    object OnRemindersClick : MovementListEvent()
    data class OnMovementClick(val movementId: String) : MovementListEvent()
    object OnStart : MovementListEvent()
    object OnStop : MovementListEvent()
}