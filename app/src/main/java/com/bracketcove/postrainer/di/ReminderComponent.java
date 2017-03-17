package com.bracketcove.postrainer.di;

import com.bracketcove.postrainer.ApplicationModule;
import com.bracketcove.postrainer.data.reminder.ReminderSource;
import com.bracketcove.postrainer.schedulers.Scheduler;
import com.bracketcove.postrainer.util.BaseScheduler;

import javax.inject.Singleton;
import dagger.Component;

/**
 * Created by Ryan on 16/03/2017.
 */

@Singleton
@Component(modules = {ApplicationModule.class, ReminderModule.class} )
public interface ReminderComponent {
    ReminderSource reminderSource();
    BaseScheduler scheduler();
}
