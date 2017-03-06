package com.bracketcove.postrainer.util;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

/**
 * This Scheduler is for use during Tests which require immediate Schedulers
 * Created by Ryan on 05/03/2017.
 */

public class ImmediateSchedulerProvider implements BaseSchedulerProvider {
    @Override
    public Scheduler computation() {
        return Schedulers.trampoline();
    }

    @Override
    public Scheduler io() {
        return Schedulers.trampoline();
    }

    @Override
    public Scheduler ui() {
        return Schedulers.trampoline();
    }
}
