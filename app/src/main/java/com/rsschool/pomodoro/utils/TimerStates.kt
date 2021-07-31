package com.rsschool.pomodoro.utils

typealias State = TimerStates

enum class TimerStates {
    CREATED,
    LAUNCHED,
    STOPED,
    RESUMED,
    FINISHED
}