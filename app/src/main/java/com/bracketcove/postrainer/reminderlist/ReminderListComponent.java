package com.bracketcove.postrainer.reminderlist;

import com.bracketcove.postrainer.di.ReminderComponent;
import com.bracketcove.postrainer.util.FragmentScoped;

import dagger.Component;

/**
 * Since ReminderDetailComponent is dependent upon the ReminderRepository, when must make
 * satisfy the dependency by supplying TaskRepositoryComponent.class.
 * Created by Ryan on 16/03/2017.
 */
@FragmentScoped
@Component(dependencies = ReminderComponent.class, modules = ReminderListPresenterModule.class)
public interface ReminderListComponent {

    void inject(ReminderListActivity reminderListActivity);

}
