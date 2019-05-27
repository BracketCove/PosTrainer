package com.wiseassblog.domain

import com.wiseassblog.domain.domainmodel.Alarm

internal fun getAlarm(
    id: String = "8675309",
    title: String = "Lunch Break",
    active: Boolean = true,
    vibrateOnly: Boolean = false,
    renewAutomatically: Boolean = false,
    hourOfDay: Int = 12,
    minuteOfDay: Int = 30
) = Alarm(
    id,
    title,
    active,
    vibrateOnly,
    renewAutomatically,
    hourOfDay,
    minuteOfDay
)

