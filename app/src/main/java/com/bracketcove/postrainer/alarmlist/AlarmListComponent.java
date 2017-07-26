package com.bracketcove.postrainer.alarmlist;

import com.bracketcove.postrainer.dependencyinjection.components.ApplicationComponent;
import com.bracketcove.postrainer.util.FragmentScoped;

import dagger.Component;

/**
 * Since AlarmDetailComponent is dependent upon the ReminderRepository, when must make
 * satisfy the dependency by supplying TaskRepositoryComponent.class.
 * Created by Ryan on 16/03/2017.
 */
@FragmentScoped
@Component(dependencies = ApplicationComponent.class,
        modules = AlarmListPresenterModule.class)
public interface AlarmListComponent {

    void inject(AlarmListFragment alarmListFragment);

}
