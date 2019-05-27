package com.bracketcove.postrainer.alarmlist

import com.bracketcove.postrainer.ALARM_CANCELLED
import com.bracketcove.postrainer.ALARM_SET
import com.bracketcove.postrainer.BaseLogic
import com.bracketcove.postrainer.ERROR_GENERIC
import com.bracketcove.postrainer.dependencyinjection.AndroidDependencyProvider
import com.wiseassblog.common.ResultWrapper
import com.wiseassblog.domain.domainmodel.Alarm
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * Retrieves a List of any Reminders which are Present in AlarmDatabase (Realm Database), and
 * displays passes them to the View.
 * Created by Ryan on 05/03/2017.
 */

class AlarmListLogic(
    private val view: AlarmListContract.View,
    //Service Locator
    private val provider: AndroidDependencyProvider,
    dispatcher: CoroutineDispatcher
) : BaseLogic<AlarmListEvent>(dispatcher) {

    //Note: these come from BaseLogic base abstract class
    override val coroutineContext: CoroutineContext
        get() = main + jobTracker

    override fun handleEvent(eventType: AlarmListEvent) {
        when (eventType) {
            is AlarmListEvent.OnStart -> getAlarmList()
            is AlarmListEvent.OnSettingsClick -> showSettings()
            is AlarmListEvent.OnCreateButtonClick -> createAlarm()
            is AlarmListEvent.OnAlarmToggled -> onToggle(eventType.isActive, eventType.alarm)
            is AlarmListEvent.OnAlarmIconClick -> onIconClick(eventType.alarm)
        }
    }

    private fun onIconClick(alarm: Alarm) = launch {

        if (alarm.isActive) provider.cancelAlarm.execute(alarm)
        //since there isn't much I can do if this thing fails, not bothering
        //to check the result currently

        view.startAlarmDetailActivity(alarm.alarmId!!)
    }

    private fun onToggle(active: Boolean, alarm: Alarm) {
        if (active) setAlarm(alarm)
        else cancelAlarm(alarm)
    }

    private fun cancelAlarm(alarm: Alarm) = launch {
        val result = provider.cancelAlarm.execute(alarm)

        when (result) {
            is ResultWrapper.Success -> alarmCancelled()
            is ResultWrapper.Error -> handleError()
        }
    }

    private fun alarmCancelled() {
        view.showMessage(ALARM_CANCELLED)
    }

    private fun setAlarm(alarm: Alarm) = launch {
        val result = provider.setAlarm.execute(alarm)

        when (result) {
            is ResultWrapper.Success -> alarmSet()
            is ResultWrapper.Error -> handleError()
        }
    }

    private fun alarmSet() {
        view.showMessage(ALARM_SET)
    }

    private fun createAlarm() {
        view.startAlarmDetailActivity("")
    }

    private fun showSettings() {
        view.startSettingsActivity()
    }

    private fun getAlarmList() = launch {
        val result = provider.getAlarmList.execute()

        when (result){
            is ResultWrapper.Success -> updateView(result.value)
            is ResultWrapper.Error -> {
                view.setNoAlarmListDataFound()
                handleError()
            }
        }
    }

    private fun handleError() {
            view.showMessage(ERROR_GENERIC)
    }

    private fun updateView(value: List<Alarm>) {
        if (value.size == 0) view.setNoAlarmListDataFound()
        else view.setAlarmListData(value)
    }


}
