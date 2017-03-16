package com.bracketcove.postrainer.data.reminder;


import com.bracketcove.postrainer.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;
import dagger.Provides;

/**
 * Since we want our Repository to be globally accesible (it's used across multiple features
 * of the App)
 * Created by Ryan on 16/03/2017.
 */
@Singleton
@Component(modules = {ReminderRepositoryModule.class, ApplicationModule.class})
public interface ReminderRepositoryComponent {

    ReminderSource getReminderRepository();
}
