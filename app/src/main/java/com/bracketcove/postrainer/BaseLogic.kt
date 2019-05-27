package com.bracketcove.postrainer

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

abstract class BaseLogic<in EventType>(protected val main: CoroutineDispatcher): CoroutineScope {
    protected val jobTracker: Job = Job()

    abstract fun handleEvent(eventType: EventType)

}