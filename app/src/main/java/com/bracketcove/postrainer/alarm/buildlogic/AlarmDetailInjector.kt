package com.bracketcove.postrainer.alarm.buildlogic

import android.app.Application
import androidx.lifecycle.ViewModelProviders
import com.bracketcove.postrainer.alarm.AlarmDetailFragment
import com.bracketcove.postrainer.alarm.AlarmDetailLogic
import com.bracketcove.postrainer.alarm.AlarmViewModel
import com.bracketcove.postrainer.dependencyinjection.AndroidDependencyProvider
import com.wiseassblog.domain.domainmodel.Alarm
import kotlinx.coroutines.Dispatchers

object AlarmDetailInjector {

    internal fun provideLogic(
        application: Application,
        view: AlarmDetailFragment,
        alarmId: String?
    ): AlarmDetailLogic {
        return AlarmDetailLogic(
            view,
            ViewModelProviders.of(view)
                .get(AlarmViewModel::class.java)
                .also {
                    it.setAlarm(
                        Alarm(alarmId = alarmId)
                    )
                },
            AndroidDependencyProvider(application),
            Dispatchers.Main
        )
    }
}