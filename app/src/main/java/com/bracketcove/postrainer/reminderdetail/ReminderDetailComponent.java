package com.bracketcove.postrainer.reminderdetail;

import dagger.Component;

/**
 * Created by Ryan on 10/03/2017.
 */

@Component(modules = ReminderDetailPresenterModule.class)
public interface ReminderDetailComponent {

    //ReminderDetailPresenter getReminderDetailPresenter();
        void inject(ReminderDetailFragment reminderDetailFragment);
}
