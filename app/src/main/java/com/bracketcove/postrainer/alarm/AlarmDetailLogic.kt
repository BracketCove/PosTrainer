package com.bracketcove.postrainer.alarm


import com.bracketcove.postrainer.ALARM_DELETED
import com.bracketcove.postrainer.ALARM_UPDATED
import com.bracketcove.postrainer.BaseLogic
import com.bracketcove.postrainer.ERROR_GENERIC
import com.bracketcove.postrainer.dependencyinjection.AndroidDependencyProvider
import com.wiseassblog.common.ResultWrapper
import com.wiseassblog.common.getFormattedDate
import com.wiseassblog.domain.domainmodel.Alarm
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class AlarmDetailLogic(
    private val view: AlarmDetailContract.View,
    private val viewModel: AlarmDetailContract.ViewModel,
    private val provider: AndroidDependencyProvider,
    dispatcher: CoroutineDispatcher
) : BaseLogic<AlarmDetailEvent>(dispatcher) {

    //Note: these come from BaseLogic base abstract class
    override val coroutineContext: CoroutineContext
        get() = main + jobTracker

    override fun handleEvent(eventType: AlarmDetailEvent) {
        when (eventType) {
            is AlarmDetailEvent.OnStart -> onStart()
            is AlarmDetailEvent.OnDoneIconPress -> onDone()
            is AlarmDetailEvent.OnBackIconPress -> onBack()
            is AlarmDetailEvent.OnDeleteIconPress -> onDelete()
            is AlarmDetailEvent.OnDeleteConfirmed -> onDeleteConfirmed()
        }
    }

    private fun onDeleteConfirmed() = launch {
        val result = provider.deleteAlarm.execute(
            viewModel.getAlarm()!!
        )

        when (result) {
            is ResultWrapper.Success -> onDeleted()
        }

    }

    private fun onDeleted() {
        view.showMessage(ALARM_DELETED)
        view.startAlarmListActivity()
    }

    private fun onDelete() {
        view.showDeleteConfirm()
    }

    private fun onBack() {
        view.startAlarmListActivity()
    }

    private fun onDone() = launch {
        val alarmUpdate = view.getState().copy(
            alarmId = viewModel.getAlarm()!!.alarmId
        )

        val result = provider.updateOrCreateAlarm.execute(alarmUpdate)

        when (result) {
            is ResultWrapper.Success -> onUpdate()
            is ResultWrapper.Error -> handleError()
        }
    }

    private fun onUpdate() {
        view.showMessage(ALARM_UPDATED)
        view.startAlarmListActivity()
    }

    private fun onStart() = launch {
        val alarmId = viewModel.getAlarm()?.alarmId
        if (alarmId != null && alarmId != "") {
            val result = provider.getAlarm.execute(alarmId)

            when (result) {
                is ResultWrapper.Success -> updateView(result.value)
                is ResultWrapper.Error -> handleError()
            }
        } else if (alarmId == "") {
            viewModel.setAlarm(
                viewModel.getAlarm()!!
                    .copy(alarmId = getFormattedDate())
                    .also {
                        updateView(it)
                    }
            )

        } else {
            handleError()
        }
    }

    private fun handleError() {
        view.showMessage(ERROR_GENERIC)
        view.startAlarmListActivity()
    }

    private fun updateView(alarm: Alarm) {
        viewModel.setAlarm(alarm)
        view.setAlarmTitle(alarm.alarmTitle)
        view.setPickerTime(alarm.hourOfDay, alarm.minute)
        view.setVibrateOnly(alarm.isVibrateOnly)
        view.setRenewAutomatically(alarm.isRenewAutomatically)
    }

}
