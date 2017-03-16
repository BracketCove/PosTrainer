package com.bracketcove.postrainer.data.reminder;

import com.bracketcove.postrainer.data.reminder.ReminderRepository;
import com.bracketcove.postrainer.data.reminder.ReminderSource;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * This implementation of ReminderRespositoryModule supplues us with the real implementation of
 * the ReminderRespository during Production
 * Created by Ryan on 15/03/2017.
 */
@Module
public class ReminderRepositoryModule {
    //@Local this may need to be below @Binds. I'm not sure if we need it since there's
    //no remote datasource

    @Singleton
    @Provides
    public ReminderSource provideReminderSource(ReminderRepository repository){
     return new ReminderRepository();
    }

}
