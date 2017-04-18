package com.bracketcove.postrainer;

import android.app.Application;

import com.bracketcove.postrainer.dependencyinjection.ApplicationComponent;
import com.bracketcove.postrainer.dependencyinjection.ApplicationModule;
import com.bracketcove.postrainer.dependencyinjection.DaggerReminderComponent;
import com.bracketcove.postrainer.dependencyinjection.ReminderComponent;
import com.squareup.leakcanary.LeakCanary;

import io.realm.Realm;
import io.realm.RealmConfiguration;


public class PostrainerApplication extends Application {
    private ApplicationComponent applicationComponent;
    private ReminderComponent reminderComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        reminderComponent = DaggerReminderComponent
                .builder()
                .applicationModule(new ApplicationModule(getApplicationContext()))
                .build();

    }

    private void initializeRealm(){
        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }

    private void initializeLeakCanary(){
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
    }

    public ReminderComponent getReminderComponent(){
        return reminderComponent;
    }

    public ApplicationComponent getApplicationComponent() {
        return this.applicationComponent;
    }
}
