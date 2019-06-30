package com.wiseassblog.androiddata.data.reminderdatabase

import com.wiseassblog.androiddata.data.realmmodel.RealmReminder
import com.wiseassblog.androiddata.data.toRealmReminder
import com.wiseassblog.androiddata.data.toReminder
import com.wiseassblog.common.ReminderServiceException
import com.wiseassblog.common.ResultWrapper
import com.wiseassblog.domain.domainmodel.Reminder
import com.wiseassblog.domain.repository.IReminderRepository
import io.realm.Realm
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 *
 */

object ReminderDatabase : IReminderRepository {

    /**
     * Used by Module Local Reminder Service
     */
    internal suspend fun makeReminderInactive(reminderId: String) = withContext(Dispatchers.IO) {
        val realm = Realm.getDefaultInstance()

        //get first alarm which matches ID
        val queryResult = realm.where<RealmReminder>(RealmReminder::class.java)
            .equalTo("reminderId", reminderId)
            .findFirst()

        if (queryResult != null) {
            val reminder = queryResult.toReminder
            updateReminders(reminder.copy(isActive = false))
        }

        realm.close()
    }

    override suspend fun deleteReminder(reminder: Reminder): ResultWrapper<Exception, Unit> =
        withContext(Dispatchers.IO) {
            val realm = Realm.getDefaultInstance()

            //get first reminder which matches ID
            val queryResult = realm.where<RealmReminder>(RealmReminder::class.java)
                .equalTo("reminderId", reminder.reminderId)
                .findFirst()

            if (queryResult == null) ResultWrapper.build { throw ReminderServiceException }
            else {
                realm.executeTransaction {
                    queryResult.deleteFromRealm()
                }

                realm.close()
                ResultWrapper.build { Unit }
            }
        }

    override suspend fun updateReminders(reminder: Reminder): ResultWrapper<Exception, Unit> =
        withContext(Dispatchers.IO) {
            val realm = Realm.getDefaultInstance()

            realm.executeTransaction {
                it.insertOrUpdate(reminder.toRealmReminder)
            }

            realm.close()
            ResultWrapper.build { Unit }
        }

    override suspend fun getReminders(): ResultWrapper<Exception, List<Reminder>> = withContext(Dispatchers.IO) {
        val realm = Realm.getDefaultInstance()

        val queryResult = realm.where<RealmReminder>(RealmReminder::class.java).findAll()

        if (queryResult.size == 0) {
            realm.close()
            ResultWrapper.build { emptyList<Reminder>() }
        } else {

            val alarmList = mutableListOf<Reminder>()
            queryResult.forEach {
                alarmList.add(it.toReminder)
            }

            realm.close()

            ResultWrapper.build { alarmList }
        }
    }

    override suspend fun getReminderById(alarmId: String): ResultWrapper<Exception, Reminder> =
        withContext(Dispatchers.IO) {
            val realm = Realm.getDefaultInstance()

            //get first alarm which matches ID
            val queryResult = realm.where<RealmReminder>(RealmReminder::class.java)
                .equalTo("reminderId", alarmId)
                .findFirst()

            if (queryResult == null) ResultWrapper.build { throw ReminderServiceException }
            else {
                val reminder = queryResult.toReminder
                realm.close()
                ResultWrapper.build { reminder }
            }

        }
}
