package com.bracketcove.postrainer.data.reminder;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;

/**
 * This implementation of ReminderRepositoryModule supplies us with a Fake version
 * of the FakeReminderRepository for use during TESTING
 * Created by Ryan on 15/03/2017.
 */

@Module
abstract class ReminderRepositoryModule {
    //@Local this may need to be below @Binds. I'm not sure if we need it since there's
    //no remote datasource

    @Singleton
    @Binds
    abstract ReminderSource provideReminderSource(FakeReminderRepository repository);

}