package com.bracketcove.postrainer.movementlist

import android.util.Log
import com.bracketcove.postrainer.BaseLogic
import com.bracketcove.postrainer.ERROR_GENERIC
import com.bracketcove.postrainer.dependencyinjection.AndroidMovementProvider
import com.wiseassblog.common.ResultWrapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class MovementListLogic(
    private val view: MovementListContract.View,
    private val provider: AndroidMovementProvider,
    dispatcher: CoroutineDispatcher
) : BaseLogic<MovementListEvent>(dispatcher) {

    override val coroutineContext: CoroutineContext
        get() = main + jobTracker

    override fun handleEvent(eventType: MovementListEvent) {
        when (eventType) {
            is MovementListEvent.OnStart -> getMovementsData()
            is MovementListEvent.OnSettingsClick -> view.startSettingsView()
            is MovementListEvent.OnRemindersClick -> view.startRemindersView()
            is MovementListEvent.OnMovementClick -> view.startMovementView(eventType.movementId)
            is MovementListEvent.OnStop -> jobTracker.cancel()
        }
    }

    private fun getMovementsData() = launch {
        val result = provider.movementRepository.getMovements()

        when (result) {
            is ResultWrapper.Success -> view.setMovementListData(result.value)
            is ResultWrapper.Error -> {
                Log.d("MOVEMENT_LIST", result.error.toString())
                view.showMessage(ERROR_GENERIC)
                view.startRemindersView()
            }
        }
    }
}