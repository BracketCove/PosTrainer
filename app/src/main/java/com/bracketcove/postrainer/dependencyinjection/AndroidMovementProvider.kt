package com.bracketcove.postrainer.dependencyinjection

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.bracketcove.postrainer.PostrainerApplication
import com.wiseassblog.androiddata.data.movementapi.MovementAPI
import com.wiseassblog.androiddata.data.movementdatabase.MovementDatabaseImpl
import com.wiseassblog.domain.api.IMovementAPI
import com.wiseassblog.domain.dependencyproviders.MovementDependencyProvider
import com.wiseassblog.domain.repository.IMovementRepository

/**
 * Since the Movement Database is just basic CRUD, I have skipped writing Use Cases entirely. Otherwise, the Use Cases
 * would essentially just be an extra layer of abstraction over the MovementRepository anyway; and I don't expect
 * the Movements to change much
 */
class AndroidMovementProvider(application: Application) : AndroidViewModel(application), MovementDependencyProvider {
    override val movementRepository: IMovementRepository
        get() = MovementDatabaseImpl(getApplication<PostrainerApplication>().assets)

    override val movementAPI: IMovementAPI
        get() = MovementAPI
}