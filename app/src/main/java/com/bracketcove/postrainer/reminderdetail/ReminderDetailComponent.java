package com.bracketcove.postrainer.reminderdetail;

import com.bracketcove.postrainer.util.FragmentScoped;

import dagger.Component;

/**
 * Since ReminderDetailComponent is dependent upon the ReminderRepository, when must make
 * satisfy the dependency by supplying TaskRepositoryComponent.class.
 * Created by Ryan on 16/03/2017.
 */
//@FragmentScoped
//@Component(dependencies = ReminderRepositoryComponent.class,
//        modules = ReminderDetailPresenterModule.class)
public interface ReminderDetailComponent {

    //ReminderDetailFragment reminder
    //void inject(ReminderDetailFragment reminderDetailFragment);
}
