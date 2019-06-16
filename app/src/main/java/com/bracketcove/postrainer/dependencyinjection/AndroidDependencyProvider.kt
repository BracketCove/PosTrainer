package com.bracketcove.postrainer.dependencyinjection

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.bracketcove.postrainer.PostrainerApplication
import com.wiseassblog.androiddata.data.reminderdatabase.ReminderDatabase
import com.wiseassblog.androiddata.data.reminderapi.ReminderApiImpl
import com.wiseassblog.domain.DependencyProvider
import com.wiseassblog.domain.repository.IReminderAPI
import com.wiseassblog.domain.repository.IReminderRepository
import com.wiseassblog.domain.usecase.*

class AndroidDependencyProvider(application: Application) : AndroidViewModel(application), DependencyProvider {

    internal val cancelReminder: CancelReminder by lazy {
        CancelReminder(reminderService(), reminderRepository())
    }
    internal val deleteReminder: DeleteReminder by lazy {
        DeleteReminder(reminderRepository())
    }

    internal val getReminder: GetReminder by lazy {
        GetReminder(reminderRepository())
    }

    internal val getReminderList: GetReminderList by lazy {
        GetReminderList(reminderRepository())
    }

    internal val setReminder: SetReminder by lazy {
        SetReminder(reminderService(), reminderRepository())
    }

    internal val updateOrCreateReminder: UpdateOrCreateReminder by lazy {
        UpdateOrCreateReminder(reminderRepository())
    }


    override val reminderRepository: IReminderRepository
        get() = reminderRepository()

    override val reminderAPI: IReminderAPI
        get() = reminderService()

    private fun reminderService(): IReminderAPI {
        val app = getApplication<PostrainerApplication>()
        return ReminderApiImpl(
            app
        )
    }

    private fun reminderRepository(): IReminderRepository = ReminderDatabase

}