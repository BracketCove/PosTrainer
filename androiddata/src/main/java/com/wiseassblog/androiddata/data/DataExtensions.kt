package com.wiseassblog.androiddata.data

import com.wiseassblog.androiddata.data.realmmodel.RealmAlarm
import com.wiseassblog.domain.domainmodel.Alarm

//alarm.alarmId = realmAlarm!!.alarmId
//alarm.isActive = realmAlarm.isActive
//alarm.isRenewAutomatically = realmAlarm.isRenewAutomatically
//alarm.isVibrateOnly = realmAlarm.isVibrateOnly
//alarm.hourOfDay = realmAlarm.hourOfDay
//alarm.minute = realmAlarm.minute
//alarm.alarmTitle = realmAlarm.alarmTitle

internal val RealmAlarm.toAlarm: Alarm
    get() = Alarm(
        this.alarmId,
        this.alarmTitle,
        this.isActive,
        this.isVibrateOnly,
        this.isRenewAutomatically,
        this.hourOfDay,
        this.minute
    )

internal val Alarm.toRealmAlarm: RealmAlarm
    get() = RealmAlarm(
        this.alarmId,
        this.alarmTitle,
        this.isActive,
        this.isVibrateOnly,
        this.isRenewAutomatically,
        this.hourOfDay,
        this.minute
    )