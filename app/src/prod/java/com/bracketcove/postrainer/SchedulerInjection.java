package com.bracketcove.postrainer;

import com.bracketcove.postrainer.util.BaseSchedulerProvider;
import com.bracketcove.postrainer.util.ImmediateSchedulerProvider;
import com.bracketcove.postrainer.util.SchedulerProvider;

public class SchedulerInjection {
    public static BaseSchedulerProvider provideSchedulerProvider(){
        return SchedulerProvider.getInstance();
    }
}