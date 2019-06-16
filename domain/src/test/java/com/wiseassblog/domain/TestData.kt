package com.wiseassblog.domain

import com.wiseassblog.domain.domainmodel.Reminder

internal fun getReminder(
    id: String = "8675309",
    title: String = "Lunch Break",
    active: Boolean = true,
    vibrateOnly: Boolean = false,
    renewAutomatically: Boolean = false,
    hourOfDay: Int = 12,
    minuteOfDay: Int = 30
) = Reminder(
    id,
    title,
    active,
    vibrateOnly,
    renewAutomatically,
    hourOfDay,
    minuteOfDay
)

