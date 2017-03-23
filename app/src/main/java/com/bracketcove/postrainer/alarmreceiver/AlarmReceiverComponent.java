package com.bracketcove.postrainer.alarmreceiver;

import com.bracketcove.postrainer.data.alarm.AlarmComponent;
import com.bracketcove.postrainer.data.reminder.ReminderComponent;
import com.bracketcove.postrainer.util.FragmentScoped;

import dagger.Component;

/**
 * Since ReminderDetailComponent is dependent upon the ReminderRepository, when must make
 * satisfy the dependency by supplying TaskRepositoryComponent.class.
 * Created by Ryan on 16/03/2017.
 */
@FragmentScoped
@Component(dependencies = {ReminderComponent.class, AlarmComponent.class},
       modules = {ApplicationModuleAlarmReceiverPresenterModule.class})
public interface AlarmReceiverComponent {
    void inject(AlarmReceiverFragment alarmReceiverFragment);
}
