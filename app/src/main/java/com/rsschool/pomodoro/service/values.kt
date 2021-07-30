package com.rsschool.pomodoro.service

typealias Command = ProcessCommands

enum class ProcessCommands(val value: String) {
    INVALID("INVALID"),
    START("START_COMMAND"),
    STOP("STOP_COMMAND")
}

var launchedTimerId: Int = 0

const val NOTIFICATION_ID = 777

/** Intent Keys */
const val COMMAND_ID = "COMMAND_ID"
const val TIMER_ID = "TIMER_ID"