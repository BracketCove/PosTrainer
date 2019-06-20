package com.bracketcove.postrainer.movementlist.buildlogic

import android.app.Application
import com.bracketcove.postrainer.dependencyinjection.AndroidMovementProvider
import com.bracketcove.postrainer.movementlist.MovementListFragment
import com.bracketcove.postrainer.movementlist.MovementListLogic
import kotlinx.coroutines.Dispatchers

object MovementListInjector {

    internal fun provideLogic(application: Application, view: MovementListFragment): MovementListLogic {
        return MovementListLogic(
            view,
            AndroidMovementProvider(application),
            Dispatchers.Main
        )
    }
}