package com.bracketcove.postrainer.dependencyinjection;

import com.bracketcove.postrainer.data.alarm.AlarmService;
import com.bracketcove.postrainer.data.alarm.AlarmSource;
import com.bracketcove.postrainer.data.reminder.ReminderService;
import com.bracketcove.postrainer.data.reminder.ReminderSource;
import com.bracketcove.postrainer.util.BaseSchedulerProvider;
import com.bracketcove.postrainer.util.FragmentScoped;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Ryan on 12/04/2017.
 */
@Singleton
@Component(modules = {ReminderModule.class, ApplicationModule.class})
public interface ReminderComponent {

    ReminderSource getReminderSource();
    AlarmSource getAlarmSource();
    BaseSchedulerProvider getSchedulerProvider();
}
