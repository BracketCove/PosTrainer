package com.bracketcove.postrainer.movement.buildlogic

import android.app.Application
import androidx.lifecycle.ViewModelProviders
import com.bracketcove.postrainer.dependencyinjection.AndroidMovementProvider
import com.bracketcove.postrainer.movement.MovementFragment
import com.bracketcove.postrainer.movement.MovementLogic
import com.bracketcove.postrainer.movement.MovementViewModel
import kotlinx.coroutines.Dispatchers

object MovementInjector {

    internal fun provideLogic(
        application: Application,
        view: MovementFragment
    ): MovementLogic {
        return MovementLogic(
            view,
            ViewModelProviders.of(view)
                .get(MovementViewModel::class.java),
            AndroidMovementProvider(application),
            Dispatchers.Main
        )
    }
}