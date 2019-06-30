package com.bracketcove.postrainer.movement

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wiseassblog.domain.domainmodel.Movement

class MovementViewModel(
    private val movementState: MutableLiveData<Movement> = MutableLiveData(),
    private var currentImageIndex: Int = 0
): ViewModel(), MovementContract.ViewModel {
    /**
     * Basically this says if the index is out of bounds (or some other issue), return the first
     */
    override fun getImageResource(index: Int): String {
       val resource = movementState.value?.imageResourceNames?.getOrNull(index)
        if (resource == null){
            //if this fails, c'est pas bon
           val firstResource = movementState.value?.imageResourceNames?.getOrNull(0) ?: "ic_alarm_black_48dp"
            currentImageIndex = 0
            return firstResource
        } else {
            currentImageIndex = index
            return resource
        }
    }

    override fun getCurrentIndex(): Int {
        return currentImageIndex
    }

    override fun setMovement(movement: Movement) {
        movementState.value = movement
    }

    override fun getMovement(): Movement? = movementState.value
}