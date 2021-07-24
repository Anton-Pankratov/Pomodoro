package com.rsschool.pomodoro.utils

enum class TimeUnits(val min: Int, val max: Int, val default: Int) {
    HOUR(0, 24, 0),
    MINUTE(0, 59, 20),
    SECOND(0, 59, 0)
}