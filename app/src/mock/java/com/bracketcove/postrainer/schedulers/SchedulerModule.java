package com.bracketcove.postrainer.schedulers;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Ryan on 10/03/2017.
 */
@Module
public class SchedulerModule {

    public static final String JOB = "JOB";
    public static final String UI = "UI";
    public static final String IO = "IO";
    public static final String TRAMPOLINE = "TRAMPOLINE";



    @Provides
    @Singleton
    @Named(JOB)
    public Scheduler provideJobScheduler() {
        return Schedulers.trampoline();
    }

    @Provides
    @Singleton
    @Named(UI)
    public Scheduler provideUIScheduler() {
        return Schedulers.trampoline();
    }

    @Provides
    @Singleton
    @Named(IO)
    public Scheduler provideIOScheduler() {
        return Schedulers.trampoline();
    }


}