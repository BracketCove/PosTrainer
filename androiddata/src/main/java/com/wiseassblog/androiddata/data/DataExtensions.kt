package com.wiseassblog.androiddata.data

import com.wiseassblog.androiddata.data.realmmodel.RealmAlarm
import com.wiseassblog.domain.domainmodel.Alarm

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

