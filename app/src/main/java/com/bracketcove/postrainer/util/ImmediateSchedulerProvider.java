package com.bracketcove.postrainer.util;

import android.support.annotation.Nullable;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

/**
 * This Scheduler is for use during Tests which require immediate Schedulers
 * Created by Ryan on 05/03/2017.
 */

public class ImmediateSchedulerProvider implements BaseSchedulerProvider {
    @Nullable
    private static ImmediateSchedulerProvider INSTANCE;

    // Prevent direct instantiation.
    private ImmediateSchedulerProvider() {
    }

    public static synchronized BaseSchedulerProvider getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ImmediateSchedulerProvider();
        }
        return INSTANCE;
    }

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
