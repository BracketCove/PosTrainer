package com.bracketcove.postrainer.reminderlist.buildlogic

import android.app.Application
import com.bracketcove.postrainer.reminderlist.ReminderListFragment
import com.bracketcove.postrainer.reminderlist.ReminderListLogic
import com.bracketcove.postrainer.dependencyinjection.AndroidReminderProvider
import kotlinx.coroutines.Dispatchers

object ReminderListInjector {

    internal fun provideLogic(application: Application, view: ReminderListFragment): ReminderListLogic {
        return ReminderListLogic(
            view,
            AndroidReminderProvider(application),
            Dispatchers.Main
        )
    }
}