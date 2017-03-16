package com.bracketcove.postrainer.util;


import com.bracketcove.postrainer.ApplicationModule;
import com.bracketcove.postrainer.schedulers.SchedulerModule;

import javax.inject.Singleton;

import dagger.Component;
import dagger.Provides;

/**
 * Since we want our Schedulers to be globally accessible (it's used across multiple features
 * of the App), we make it Singleton Scope (a.k.a. ApplicationScope)
 * Created by Ryan on 16/03/2017.
 */
@Singleton
@Component(modules = {SchedulerModule.class, ApplicationModule.class})
public interface SchedulerComponent {

    //@Provides
    //BaseScheduler getScheduler();
}
