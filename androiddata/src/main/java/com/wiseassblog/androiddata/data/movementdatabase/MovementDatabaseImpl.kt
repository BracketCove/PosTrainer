package com.wiseassblog.androiddata.data.movementdatabase

import com.wiseassblog.common.ResultWrapper
import com.wiseassblog.domain.domainmodel.Movement
import com.wiseassblog.domain.repository.IMovementRepository

object MovementDatabaseImpl : IMovementRepository {
    override suspend fun getMovements(): ResultWrapper<Exception, List<Movement>> {
        return ResultWrapper.build {
            listOf(
                getMovement(),
                getMovement(),
                getMovement(),
                getMovement()
            )
        }
    }

    override suspend fun getMovementById(movementId: String): ResultWrapper<Exception, Movement> {
        return ResultWrapper.build {
            getMovement()
        }
    }


    private fun getMovement() = Movement(
        "Passive Hang",
        listOf("ic_chest", "ic_shoulders", "ic_neck", "ic_back"),
        "2 Sets/Day",
        "30-60 Seconds",
        true,
        listOf("ic_launcher_v2"),
        "Easy",
        "www.youtube.com",
        "description_passive_hang",
        "instruction_passive_hang"
    )
}