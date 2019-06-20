package com.bracketcove.postrainer.movement

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wiseassblog.domain.domainmodel.Movement

class MovementViewModel(
    private val movementState: MutableLiveData<Movement> = MutableLiveData(),
    //passed in at the beginning
    private val movementId: MutableLiveData<String> = MutableLiveData()
): ViewModel(), MovementContract.ViewModel {
    override fun setMovement(movement: Movement) {
        movementState.value = movement
    }

    override fun getMovement(): Movement? = movementState.value
}