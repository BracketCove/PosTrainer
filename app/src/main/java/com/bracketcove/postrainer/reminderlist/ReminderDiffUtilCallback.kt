package com.bracketcove.postrainer.reminderlist


import androidx.recyclerview.widget.DiffUtil
import com.wiseassblog.domain.domainmodel.Reminder

class ReminderDiffUtilCallback : DiffUtil.ItemCallback<Reminder>() {
    override fun areItemsTheSame(
        oldItem: Reminder,
        newItem: Reminder
    ): Boolean {
        return oldItem.reminderId == newItem.reminderId
    }

    override fun areContentsTheSame(
        oldItem: Reminder,
        newItem: Reminder
    ): Boolean {
        return oldItem.reminderId == newItem.reminderId
    }
}