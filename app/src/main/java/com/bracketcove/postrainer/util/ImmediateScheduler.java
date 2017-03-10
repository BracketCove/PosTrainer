package com.bracketcove.postrainer.util;

import android.support.annotation.Nullable;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

/**
 * This Scheduler is for use during Tests which require immediate Schedulers
 * Created by Ryan on 05/03/2017.
 */

public class ImmediateScheduler implements BaseScheduler {
    @Nullable
    private static ImmediateScheduler INSTANCE;

    // Prevent direct instantiation.
    private ImmediateScheduler() {
    }

    public static synchronized BaseScheduler getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ImmediateScheduler();
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
