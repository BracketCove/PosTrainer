package com.bracketcove.postrainer.alarmreceiver;

import com.bracketcove.postrainer.data.reminder.ReminderRepositoryComponent;
import com.bracketcove.postrainer.reminderdetail.ReminderDetailPresenterModule;
import com.bracketcove.postrainer.util.FragmentScoped;

import dagger.Component;

/**
 * Since ReminderDetailComponent is dependent upon the ReminderRepository, when must make
 * satisfy the dependency by supplying TaskRepositoryComponent.class.
 * Created by Ryan on 16/03/2017.
 */
@FragmentScoped
@Component(dependencies = ReminderRepositoryComponent.class,
        modules = AlarmPresenterModule.class)
public interface AlarmComponent {

    //ReminderDetailFragment reminder
    //void inject(ReminderDetailFragment reminderDetailFragment);
}
