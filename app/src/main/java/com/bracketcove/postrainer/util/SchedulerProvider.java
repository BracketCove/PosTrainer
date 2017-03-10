package com.bracketcove.postrainer.util;

import android.support.annotation.Nullable;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * This SchedulerProvider is for use during Device Tests/Production
 * Created by Ryan on 05/03/2017.
 */

public class SchedulerProvider implements BaseSchedulerProvider {
    @Nullable
    private static SchedulerProvider INSTANCE;

    // Prevent direct instantiation.
    private SchedulerProvider() {
    }

    public static synchronized BaseSchedulerProvider getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SchedulerProvider();
        }
        return INSTANCE;
    }

    @Override
    public Scheduler computation() {
        return Schedulers.computation();
    }

    @Override
    public Scheduler io() {
        return Schedulers.io();
    }

    @Override
    public Scheduler ui() {
        return AndroidSchedulers.mainThread();
    }
}