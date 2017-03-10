package com.bracketcove.postrainer.util;

import io.reactivex.Scheduler;

/**
 * Created by Ryan on 05/03/2017.
 */

public interface BaseScheduler {

    Scheduler computation();

    Scheduler io();

    Scheduler ui();
}
