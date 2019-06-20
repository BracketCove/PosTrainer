package com.bracketcove.postrainer.reminder.buildlogic

import android.app.Application
import androidx.lifecycle.ViewModelProviders
import com.bracketcove.postrainer.dependencyinjection.AndroidReminderProvider
import com.bracketcove.postrainer.reminder.ReminderDetailFragment
import com.bracketcove.postrainer.reminder.ReminderDetailLogic
import com.bracketcove.postrainer.reminder.ReminderViewModel
import com.wiseassblog.domain.domainmodel.Reminder
import kotlinx.coroutines.Dispatchers

object ReminderDetailInjector {

    internal fun provideLogic(
        application: Application,
        view: ReminderDetailFragment,
        alarmId: String?
    ): ReminderDetailLogic {
        return ReminderDetailLogic(
            view,
            ViewModelProviders.of(view)
                .get(ReminderViewModel::class.java)
                .also {
                    it.setReminder(
                        Reminder(reminderId = alarmId)
                    )
                },
            AndroidReminderProvider(application),
            Dispatchers.Main
        )
    }
}