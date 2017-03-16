package com.bracketcove.postrainer;

import android.app.Application;

import com.bracketcove.postrainer.data.reminder.DaggerReminderRepositoryComponent;
import com.bracketcove.postrainer.data.reminder.ReminderRepositoryComponent;
import com.bracketcove.postrainer.util.DaggerSchedulerComponent;
import com.bracketcove.postrainer.util.SchedulerComponent;

/**
 * Please note that the following comment uses the word *component* to refer to parts of
 * this an App which is organized by component (e.g. statistics, taskdetail, and task packages).
 * For the moment, don't conflate the word with a DaggerComponent.
 *
 *Since some *parts* of this application's architecture are shared by multiple components (e.g.
 * TaskRepository.java), and some parts are unique to specific components (for example, each
 * presenter requires a unique View interface @see StatisticsPresnterModule.java), it makes sense
 * seperate such *parts* by accesibility (i.e. Scope).
 *
 * TaskRepository.java is shared by several components, which means that we can place it in a spot
 * where it is easily accesible by any component which needs it. This may
 *
 *
 * Goal number 1: Figure out what dependencies need to be Application scope (which in annotations
 * (at)Singleton or (at)ApplicationScoped
 */

public class PostrainerApplication extends Application {
    private ReminderRepositoryComponent reminderRepositoryComponent;
    private SchedulerComponent schedulerComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        reminderRepositoryComponent = DaggerReminderRepositoryComponent
                .builder()
                .applicationModule(new ApplicationModule(getApplicationContext()))
                .build();

        schedulerComponent = DaggerSchedulerComponent
                .builder()
                .applicationModule(new ApplicationModule(getApplicationContext()))
                .build();
    }

    public ReminderRepositoryComponent getReminderRepositoryComponent(){
        return this.reminderRepositoryComponent;
    }

    public SchedulerComponent getSchedulerComponent(){
        return this.schedulerComponent;
    }
}
