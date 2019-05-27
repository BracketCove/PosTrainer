package com.wiseassblog.domain

import com.wiseassblog.domain.repository.IAlarmRepository
import com.wiseassblog.domain.repository.IAlarmService

//Allows me to inject appropriate dependencies, or mocks, when calling operations on my Use Cases. Essentially the same principle as dependency injection, except that dependencies are given per function invocation as opposed to the Use Case holding on to them as properties.
interface DependencyProvider {
    val alarmRepository: IAlarmRepository
    val alarmService: IAlarmService
}