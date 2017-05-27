package com.bracketcove.postrainer.reminderdetail;

import com.bracketcove.postrainer.data.reminder.ReminderService;
import com.bracketcove.postrainer.data.reminder.ReminderSource;
import com.bracketcove.postrainer.dependencyinjection.components.ApplicationComponent;
import com.bracketcove.postrainer.dependencyinjection.components.ReminderComponent;
import com.bracketcove.postrainer.util.FragmentScoped;

import dagger.Component;

/**
 * Since ReminderDetailComponent is dependent upon the ReminderRepository, when must make
 * satisfy the dependency by supplying TaskRepositoryComponent.class.
 * Created by Ryan on 16/03/2017.
 */
@FragmentScoped
@Component(dependencies = ApplicationComponent.class,
        modules = ReminderDetailPresenterModule.class)
public interface ReminderDetailComponent {

    void inject(ReminderDetailActivity reminderDetailActivity);
}

