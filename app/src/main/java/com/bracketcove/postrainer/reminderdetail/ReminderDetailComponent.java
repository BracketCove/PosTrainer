package com.bracketcove.postrainer.reminderdetail;

import com.bracketcove.postrainer.data.reminder.ReminderSource;
import com.bracketcove.postrainer.dependencyinjection.ApplicationComponent;
import com.bracketcove.postrainer.dependencyinjection.ApplicationModule;
import com.bracketcove.postrainer.dependencyinjection.ReminderComponent;
import com.bracketcove.postrainer.util.FragmentScoped;

import dagger.Component;

/**
 * Since ReminderDetailComponent is dependent upon the ReminderRepository, when must make
 * satisfy the dependency by supplying TaskRepositoryComponent.class.
 * Created by Ryan on 16/03/2017.
 */
@FragmentScoped
@Component(dependencies = ReminderComponent.class,
        modules = ReminderDetailPresenterModule.class)
public interface ReminderDetailComponent {

    void inject(ReminderDetailActivity reminderDetailActivity);
    ReminderSource provideReminderSource();
}

