package com.bracketcove.postrainer.dependencyinjection;

import com.bracketcove.postrainer.data.alarm.AlarmService;
import com.bracketcove.postrainer.data.alarm.AlarmSource;
import com.bracketcove.postrainer.data.reminder.ReminderSource;
import com.bracketcove.postrainer.util.BaseSchedulerProvider;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Ryan on 12/04/2017.
 */
@Singleton
@Component(modules = {AlarmModule.class, ApplicationModule.class})
public interface AlarmComponent {

    void inject(AlarmSource alarmSource);

}
