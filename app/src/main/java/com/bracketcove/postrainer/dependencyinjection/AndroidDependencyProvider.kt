package com.bracketcove.postrainer.dependencyinjection

import android.app.Application
import android.content.Context
import android.media.MediaPlayer
import android.os.Vibrator
import android.provider.Settings
import androidx.lifecycle.AndroidViewModel
import com.bracketcove.postrainer.PostrainerApplication
import com.wiseassblog.androiddata.data.alarmdatabase.AlarmDatabase
import com.wiseassblog.androiddata.data.alarmservice.AlarmServiceManager
import com.wiseassblog.domain.DependencyProvider
import com.wiseassblog.domain.repository.IAlarmRepository
import com.wiseassblog.domain.repository.IAlarmService
import com.wiseassblog.domain.usecase.*

class AndroidDependencyProvider(application: Application) : AndroidViewModel(application), DependencyProvider {

    internal val cancelAlarm: CancelAlarm by lazy {
        CancelAlarm(alarmService(), alarmRepository())
    }
    internal val deleteAlarm: DeleteAlarm by lazy {
        DeleteAlarm(alarmRepository())
    }

    internal val dismissAlarm: DismissAlarm by lazy {
        DismissAlarm(alarmService(), alarmRepository())
    }

    internal val getAlarm: GetAlarm by lazy {
        GetAlarm(alarmRepository())
    }

    internal val getAlarmList: GetAlarmList by lazy {
        GetAlarmList(alarmRepository())
    }

    internal val setAlarm: SetAlarm by lazy {
        SetAlarm(alarmService(), alarmRepository())
    }

    internal val startAlarm: StartAlarm by lazy {
        StartAlarm(alarmService(), alarmRepository())
    }

    internal val updateOrCreateAlarm: UpdateOrCreateAlarm by lazy {
        UpdateOrCreateAlarm(alarmRepository())
    }


    override val alarmRepository: IAlarmRepository
        get() = alarmRepository()
    override val alarmService: IAlarmService
        get() = alarmService()

    private fun alarmService(): IAlarmService {
        val app = getApplication<PostrainerApplication>()
        return AlarmServiceManager(
            MediaPlayer.create(app, Settings.System.DEFAULT_ALARM_ALERT_URI),
            app.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator,
            app.getSystemService(Context.ALARM_SERVICE) as android.app.AlarmManager,
            app
        )
    }

    private fun alarmRepository(): IAlarmRepository = AlarmDatabase

}