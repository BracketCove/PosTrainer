package com.bracketcove.postrainer.dependencyinjection.components;

import com.bracketcove.postrainer.data.reminder.ReminderService;
import com.bracketcove.postrainer.data.reminder.ReminderSource;
import com.bracketcove.postrainer.dependencyinjection.modules.ApplicationModule;
import com.bracketcove.postrainer.dependencyinjection.modules.ReminderModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Ryan on 12/04/2017.
 */
@Singleton
@Component(modules = {ReminderModule.class, ApplicationModule.class})
public interface ReminderComponent {

    void inject(ReminderService reminderService);

}
