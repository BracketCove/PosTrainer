package com.wiseassblog.domain

import com.wiseassblog.domain.repository.IReminderAPI
import com.wiseassblog.domain.repository.IReminderRepository

//Allows me to inject appropriate dependencies, or mocks, when calling operations on my Use Cases. Essentially the same principle as dependency injection, except that dependencies are given per function invocation as opposed to the Use Case holding on to them as properties.
interface DependencyProvider {
    val reminderRepository: IReminderRepository
    val reminderAPI: IReminderAPI
}