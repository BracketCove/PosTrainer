package com.wiseassblog.domain.repository

import com.wiseassblog.common.ResultWrapper
import com.wiseassblog.domain.domainmodel.Movement

interface IMovementRepository {
    suspend fun getMovements(): ResultWrapper<Exception, List<Movement>>

    suspend fun getMovementById(movementId: String): ResultWrapper<Exception, Movement>
}