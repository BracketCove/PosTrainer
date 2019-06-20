package com.wiseassblog.domain.dependencyproviders

import com.wiseassblog.domain.api.IMovementAPI
import com.wiseassblog.domain.repository.IMovementRepository

interface MovementDependencyProvider {
    val movementRepository: IMovementRepository
    val movementAPI: IMovementAPI
}