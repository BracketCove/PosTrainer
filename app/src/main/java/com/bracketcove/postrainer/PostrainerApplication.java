package com.bracketcove.postrainer;

import android.app.Application;

import com.bracketcove.postrainer.dependencyinjection.components.AlarmComponent;
import com.bracketcove.postrainer.dependencyinjection.components.ApplicationComponent;
import com.bracketcove.postrainer.dependencyinjection.components.DaggerAlarmComponent;
import com.bracketcove.postrainer.dependencyinjection.components.DaggerApplicationComponent;
import com.bracketcove.postrainer.dependencyinjection.components.DaggerReminderComponent;
import com.bracketcove.postrainer.dependencyinjection.components.ReminderComponent;
import com.bracketcove.postrainer.dependencyinjection.modules.ApplicationModule;
import com.squareup.leakcanary.LeakCanary;

import io.realm.Realm;
import io.realm.RealmConfiguration;


public class PostrainerApplication extends Application {
    private ApplicationComponent applicationComponent;
    private ReminderComponent reminderComponent;
    private AlarmComponent alarmComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        initializeRealm();
        initializeLeakCanary();

        ApplicationModule applicationModule = new ApplicationModule(
                getApplicationContext()
        );

        applicationComponent = DaggerApplicationComponent
                .builder()
                .applicationModule(applicationModule)
                .build();

//        reminderComponent = DaggerReminderComponent
//                .builder()
//                .applicationModule(applicationModule)
//                .build();
//
//        alarmComponent = DaggerAlarmComponent
//                .builder()
//                .applicationModule(applicationModule)
//                .build();

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

    public AlarmComponent getAlarmComponent(){
        return alarmComponent;
    }

    public ApplicationComponent getApplicationComponent() {
        return this.applicationComponent;
    }
}
