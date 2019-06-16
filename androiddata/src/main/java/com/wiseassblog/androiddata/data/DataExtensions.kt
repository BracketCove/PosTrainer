package com.wiseassblog.androiddata.data

import com.wiseassblog.androiddata.data.realmmodel.RealmReminder
import com.wiseassblog.domain.domainmodel.Reminder
import java.util.*

internal val RealmReminder.toReminder: Reminder
    get() = Reminder(
        this.reminderId,
        this.reminderTitle,
        this.isActive,
        this.isVibrateOnly,
        this.isRenewAutomatically,
        this.hourOfDay,
        this.minute
    )

internal val Reminder.toRealmReminder: RealmReminder
    get() = RealmReminder(
        this.reminderId,
        this.reminderTitle,
        this.isActive,
        this.isVibrateOnly,
        this.isRenewAutomatically,
        this.hourOfDay,
        this.minute
    )

/**
 * Read out of the Hour and
 */
internal fun Calendar.setReminderTime(reminder: Reminder): Calendar  {
    timeInMillis = System.currentTimeMillis()
    set(Calendar.HOUR_OF_DAY, reminder.hourOfDay)
    set(Calendar.MINUTE, reminder.minute)
    return this
}
