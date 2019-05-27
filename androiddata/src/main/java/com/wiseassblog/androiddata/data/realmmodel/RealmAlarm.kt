package com.wiseassblog.androiddata.data.realmmodel

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * POKO for Realm. Using Realm, class must be "open"
 * Created by Ryan on 10/04/2016.
 */

open class RealmAlarm(
    @PrimaryKey
    var alarmId: String? = null,
    var alarmTitle: String = "",
    var isActive: Boolean = false,
    var isVibrateOnly: Boolean = false,
    var isRenewAutomatically: Boolean = false,
    var hourOfDay: Int = 0,
    var minute: Int = 0
) : RealmObject()
