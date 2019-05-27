package com.bracketcove.postrainer.alarmlist.buildlogic

import android.app.Application
import com.bracketcove.postrainer.alarmlist.AlarmListFragment
import com.bracketcove.postrainer.alarmlist.AlarmListLogic
import com.bracketcove.postrainer.dependencyinjection.AndroidDependencyProvider
import kotlinx.coroutines.Dispatchers

object AlarmListInjector {

    internal fun provideLogic(application: Application, view: AlarmListFragment): AlarmListLogic {
        return AlarmListLogic(
            view,
            AndroidDependencyProvider(application),
            Dispatchers.Main
        )
    }
}