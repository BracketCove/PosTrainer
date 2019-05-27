package com.bracketcove.postrainer

import com.bracketcove.postrainer.alarm.AlarmDetailContract
import com.wiseassblog.domain.domainmodel.Alarm

class FakeAlarmDetailView : AlarmDetailContract.View {

    var setAlarmTitleCalled = false
    var setAlarmCalledWith: String? = null

    override fun setAlarmTitle(title: String) {
        setAlarmTitleCalled = true
        setAlarmCalledWith = title

    }

    override fun setVibrateOnly(vibrateOnly: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setRenewAutomatically(renewAutomatically: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setPickerTime(hour: Int, minute: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun startAlarmListActivity() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showMessage(msg: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showDeleteConfirm() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getState(): Alarm {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}