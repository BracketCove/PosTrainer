package com.bracketcove.postrainer.movement

import com.bracketcove.postrainer.BaseLogic
import com.bracketcove.postrainer.ERROR_GENERIC
import com.bracketcove.postrainer.dependencyinjection.AndroidMovementProvider
import com.wiseassblog.common.ResultWrapper
import com.wiseassblog.domain.domainmodel.Movement
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class MovementLogic(
    private val view: MovementContract.View,
    private val viewModel: MovementContract.ViewModel,
    private val provider: AndroidMovementProvider,
    dispatcher: CoroutineDispatcher
) : BaseLogic<MovementEvent>(dispatcher) {
    override val coroutineContext: CoroutineContext
        get() = main + jobTracker

    override fun handleEvent(eventType: MovementEvent) {
        when (eventType){
            is MovementEvent.OnStart -> getMovement(eventType.movementId)
        }
    }

    private fun getMovement(movementId: String?) = launch {
        if (movementId != null && movementId != ""){
            val result = provider.movementRepository.getMovementById(movementId)

            when (result) {
                is ResultWrapper.Success -> {
                    viewModel.setMovement(result.value)
                    updateView(result.value)
                }

                is ResultWrapper.Error -> handleError()
            }

        } else {
            handleError()
        }
    }

    private fun handleError() {
        view.showMessage(ERROR_GENERIC)
        view.startMovementListView()
    }

    //TODO update this function so that it passes the whole set of images in
    private fun updateView(mov: Movement) {
        view.setTootlbarTitle(mov.name)
        view.setParallaxImage(mov.imageResourceNames)
        view.setTargets(mov.targets)
        view.setFrequency(mov.frequency)
        view.setIsTimed(mov.isTimeBased)
        view.setTimeOrRepetitions(mov.timeOrRepetitions)
        view.setDifficulty(mov.difficulty)
        view.setDescription(mov.descriptionResourceName)
        view.setInstructions(mov.instructionsResourceName)
    }


}