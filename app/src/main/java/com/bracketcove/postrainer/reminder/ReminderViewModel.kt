package com.bracketcove.postrainer.reminder

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wiseassblog.domain.domainmodel.Reminder

class ReminderViewModel(
   private val reminderState: MutableLiveData<Reminder> = MutableLiveData()
) : ViewModel(),
    ReminderDetailContract.ViewModel {
    override fun setReminder(reminder: Reminder) {
        reminderState.value = reminder
    }

    override fun getReminder(): Reminder? = reminderState.value
}