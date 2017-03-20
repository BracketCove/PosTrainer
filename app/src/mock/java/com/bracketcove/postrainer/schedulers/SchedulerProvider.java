package com.bracketcove.postrainer.schedulers;

import android.support.annotation.Nullable;

import com.bracketcove.postrainer.util.BaseSchedulerProvider;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

/**
 * IMPORTANT: This scheduler is for use during TESTING ONLY! This is why it is in
 * the Mock build variant. You must use Schedulers.trampoline() during tests, otherwise RxJava
 * will complain (as far as I've seen).
 *
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
