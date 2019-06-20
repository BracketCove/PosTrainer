package com.bracketcove.postrainer.dependencyinjection

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.wiseassblog.androiddata.data.reminderapi.ReminderApiImpl
import com.wiseassblog.androiddata.data.reminderdatabase.ReminderDatabase
import com.wiseassblog.domain.api.IReminderAPI
import com.wiseassblog.domain.dependencyproviders.ReminderDependencyProvider
import com.wiseassblog.domain.repository.IReminderRepository
import com.wiseassblog.domain.usecase.*

class AndroidReminderProvider(application: Application) : AndroidViewModel(application),
    ReminderDependencyProvider {

    override val reminderRepository: IReminderRepository
        get() = ReminderDatabase

    override val reminderAPI: IReminderAPI
        get() = ReminderApiImpl(
            getApplication()
        )

    internal val cancelReminder: CancelReminder by lazy {
        CancelReminder(reminderAPI, reminderRepository)
    }
    internal val deleteReminder: DeleteReminder by lazy {
        DeleteReminder(reminderRepository)
    }

    internal val getReminder: GetReminder by lazy {
        GetReminder(reminderRepository)
    }

    internal val getReminderList: GetReminderList by lazy {
        GetReminderList(reminderRepository)
    }

    internal val setReminder: SetReminder by lazy {
        SetReminder(reminderAPI, reminderRepository)
    }

    internal val updateOrCreateReminder: UpdateOrCreateReminder by lazy {
        UpdateOrCreateReminder(reminderRepository)
    }
}