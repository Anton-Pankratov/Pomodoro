package com.rsschool.pomodoro.utils

enum class TimeUnits(val min: Int, val max: Int, val default: Int) {
    HOUR(0, 23, 0),
    MINUTE(0, 59, 1),
    SECOND(0, 59, 0)
}