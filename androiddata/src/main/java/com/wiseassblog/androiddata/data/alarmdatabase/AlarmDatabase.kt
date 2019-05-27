package com.wiseassblog.androiddata.data.alarmdatabase

import com.wiseassblog.androiddata.data.realmmodel.RealmAlarm
import com.wiseassblog.androiddata.data.toAlarm
import com.wiseassblog.androiddata.data.toRealmAlarm
import com.wiseassblog.common.AlarmServiceException
import com.wiseassblog.common.ResultWrapper
import com.wiseassblog.domain.domainmodel.Alarm
import com.wiseassblog.domain.repository.IAlarmRepository
import io.realm.Realm
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Created by Ryan on 10/04/2017.
 */

object AlarmDatabase : IAlarmRepository {
    override suspend fun deleteAlarm(alarm: Alarm): ResultWrapper<Exception, Unit> = withContext(Dispatchers.IO) {
        val realm = Realm.getDefaultInstance()

        //get first alarm which matches ID
        val queryResult = realm.where<RealmAlarm>(RealmAlarm::class.java)
            .equalTo("alarmId", alarm.alarmId)
            .findFirst()

        if (queryResult == null) ResultWrapper.build { throw AlarmServiceException }
        else {
            realm.executeTransaction {
                queryResult.deleteFromRealm()
            }

            ResultWrapper.build { Unit }
        }
    }

    override suspend fun updateAlarm(alarm: Alarm): ResultWrapper<Exception, Unit> = withContext(Dispatchers.IO) {
        val realm = Realm.getDefaultInstance()

        realm.executeTransaction {
            it.insertOrUpdate(alarm.toRealmAlarm)
        }

        ResultWrapper.build { Unit }
    }

    override suspend fun getAlarms(): ResultWrapper<Exception, List<Alarm>> = withContext(Dispatchers.IO) {
        val realm = Realm.getDefaultInstance()

        val queryResult = realm.where<RealmAlarm>(RealmAlarm::class.java).findAll()

        if (queryResult.size == 0) {
            ResultWrapper.build { emptyList<Alarm>() }
        } else {

            val alarmList = mutableListOf<Alarm>()
            queryResult.forEach {
                alarmList.add(it.toAlarm)
            }
            ResultWrapper.build { alarmList }
        }
    }

    override suspend fun getAlarmById(alarmId: String): ResultWrapper<Exception, Alarm> = withContext(Dispatchers.IO) {
        val realm = Realm.getDefaultInstance()

        //get first alarm which matches ID
        val queryResult = realm.where<RealmAlarm>(RealmAlarm::class.java)
            .equalTo("alarmId", alarmId)
            .findFirst()

        if (queryResult == null) ResultWrapper.build { throw AlarmServiceException }
        else ResultWrapper.build { queryResult.toAlarm }

    }
}
