package com.bracketcove.postrainer;

import android.app.Application;

import com.bracketcove.postrainer.dependencyinjection.ApplicationComponent;
import com.bracketcove.postrainer.dependencyinjection.ApplicationModule;
import com.bracketcove.postrainer.dependencyinjection.DaggerApplicationComponent;

/**
 * Please note that the following comment uses the word *component* to refer to parts of
 * this an App which is organized by component (e.g. statistics, taskdetail, and task packages).
 * For the moment, don't conflate the word with a DaggerComponent.
 * <p>
 * Since some *parts* of this application's architecture are shared by multiple components (e.g.
 * TaskRepository.java), and some parts are unique to specific components (for example, each
 * presenter requires a unique View interface @see StatisticsPresnterModule.java), it makes sense
 * seperate such *parts* by accesibility (i.e. Scope).
 * <p>
 * TaskRepository.java is shared by several components, which means that we can place it in a spot
 * where it is easily accesible by any component which needs it. This may
 * <p>
 * <p>
 * Goal number 1: Figure out what dependencies need to be Application scope (which in annotations
 * (at)Singleton or (at)ApplicationScoped
 */

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

//        reminderComponent = DaggerReminderComponent
//                .builder()
//                .applicationModule(applicationModule)
//                .reminderModule(new ReminderModule())
//                .build();
//
//        alarmComponent = DaggerAlarmComponent
//                .builder()
//                .applicationModule(applicationModule)
//                .alarmModule(new AlarmModule(this))
//                .build();
    }

    public ApplicationComponent getApplicationComponent() {
        return this.applicationComponent;
    }
}
