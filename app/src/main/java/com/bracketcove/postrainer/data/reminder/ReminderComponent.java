package com.bracketcove.postrainer.data.reminder;

import com.bracketcove.postrainer.dependencyinjection.ApplicationModule;
import com.bracketcove.postrainer.util.BaseSchedulerProvider;

import javax.inject.Singleton;
import dagger.Component;

/**
 * Created by Ryan on 16/03/2017.
 */

@Singleton
@Component(modules = {ApplicationModule.class, ReminderModule.class} )
public interface ReminderComponent {
    ReminderSource reminderSource();
    BaseSchedulerProvider scheduler();

}
