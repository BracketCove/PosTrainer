package com.wiseassblog.domain.domainmodel

/**
 * POJO for the View
 * Created by Ryan on 10/04/2017.
 */

data class Alarm(
    val alarmId: String? = null,
    val alarmTitle: String = "Lunch Break",
    val isActive: Boolean = false,
    val isVibrateOnly: Boolean = false,
    val isRenewAutomatically: Boolean = false,
    val hourOfDay: Int = 0,
    val minute: Int = 0
)
