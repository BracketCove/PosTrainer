package com.bracketcove.postrainer

import android.app.Application
import com.bracketcove.postrainer.dependencyinjection.AndroidReminderProvider
import io.realm.Realm
import io.realm.RealmConfiguration


class PostrainerApplication: Application() {

    internal val dependencyProvider = AndroidReminderProvider(this)

    override fun onCreate() {
        super.onCreate()
        initializeRealm()



    }



    private fun initializeRealm() {
        Realm.init(this)
        val realmConfiguration = RealmConfiguration.Builder()
            .build()
        Realm.setDefaultConfiguration(realmConfiguration)
    }


}
