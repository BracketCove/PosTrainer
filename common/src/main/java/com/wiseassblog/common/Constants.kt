package com.wiseassblog.common

object ReminderServiceException: Exception()
object ReminderRepositoryException: Exception()
object MovementRepositoryException: Exception()
object MovementAPIException: Exception()

const val  REMINDER_OFF:String = "OFF"
const val  REMINDER_ON:String = "ON"