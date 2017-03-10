package com.bracketcove.postrainer.util;

import android.support.annotation.Nullable;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * This Scheduler is for use during Device Tests/Production
 * Created by Ryan on 05/03/2017.
 */

public class Scheduler implements BaseScheduler {
    @Nullable
    private static Scheduler INSTANCE;

    // Prevent direct instantiation.
    private Scheduler() {
    }

    public static synchronized BaseScheduler getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Scheduler();
        }
        return INSTANCE;
    }

    @Override
    public io.reactivex.Scheduler computation() {
        return Schedulers.computation();
    }

    @Override
    public io.reactivex.Scheduler io() {
        return Schedulers.io();
    }

    @Override
    public io.reactivex.Scheduler ui() {
        return AndroidSchedulers.mainThread();
    }
}