package com.bracketcove.postrainer;

import android.app.Application;

import com.bracketcove.postrainer.dependencyinjection.ApplicationComponent;
import com.bracketcove.postrainer.dependencyinjection.ApplicationModule;
import com.bracketcove.postrainer.dependencyinjection.DaggerApplicationComponent;
import com.squareup.leakcanary.LeakCanary;

import io.realm.Realm;
import io.realm.RealmConfiguration;


public class PostrainerApplication extends Application {
    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        ApplicationModule applicationModule = new ApplicationModule(this);

        applicationComponent = DaggerApplicationComponent
                .builder()
                .applicationModule(applicationModule)
                .build();

        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);

        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
    }

    public ApplicationComponent getApplicationComponent() {
        return this.applicationComponent;
    }
}
