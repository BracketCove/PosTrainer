package com.bracketcove.postrainer;

import android.app.Application;


import com.bracketcove.postrainer.data.alarm.AlarmComponent;
import com.bracketcove.postrainer.data.alarm.AlarmModule;
import com.bracketcove.postrainer.data.alarm.DaggerAlarmComponent;
import com.bracketcove.postrainer.data.reminder.DaggerReminderComponent;
import com.bracketcove.postrainer.data.reminder.ReminderComponent;
import com.bracketcove.postrainer.data.reminder.ReminderModule;

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
    private ReminderComponent reminderComponent;
    private AlarmComponent alarmComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        ApplicationModule applicationModule = new ApplicationModule(this);

        reminderComponent = DaggerReminderComponent
                .builder()
                .applicationModule(applicationModule)
                .reminderModule(new ReminderModule())
                .build();

        alarmComponent = DaggerAlarmComponent
                .builder()
                .applicationModule(applicationModule)
                .alarmModule(new AlarmModule(this))
                .build();
    }

    public ReminderComponent getReminderComponent() {
        return this.reminderComponent;
    }
}
