package com.bracketcove.postrainer.reminder

import com.wiseassblog.domain.domainmodel.Reminder

/**
 * Created by Ryan on 06/03/2017.
 */

interface ReminderDetailContract {
    interface View  {
        fun setReminderTitle(title:String)

        fun setVibrateOnly(vibrateOnly: Boolean)

        fun setRenewAutomatically(renewAutomatically: Boolean)

        fun setPickerTime(hour: Int, minute: Int)

        fun startReminderListActivity()

        fun showMessage(msg: String)

        fun showDeleteConfirm()

        fun getState(): Reminder
    }

    interface ViewModel {
        fun getReminder(): Reminder?
        fun setReminder(reminder: Reminder)
    }
}

sealed class ReminderDetailEvent {
    object OnBackIconPress : ReminderDetailEvent()
    object OnDoneIconPress : ReminderDetailEvent()
    object OnDeleteIconPress : ReminderDetailEvent()
    object OnDeleteConfirmed : ReminderDetailEvent()
    object OnStart : ReminderDetailEvent()
}
