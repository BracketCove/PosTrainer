package com.wiseassblog.domain.domainmodel

/**
 * POKO for the View
 * Created by Ryan on 10/04/2017.
 */

data class Reminder(
    val reminderId: String? = null,
    val reminderTitle: String = "Lunch Break",
    val isActive: Boolean = false,
    val isVibrateOnly: Boolean = false,
    val isRenewAutomatically: Boolean = false,
    val hourOfDay: Int = 0,
    val minute: Int = 0
)
