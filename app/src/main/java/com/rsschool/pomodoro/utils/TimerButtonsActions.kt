package com.rsschool.pomodoro.utils

import com.rsschool.domain.entity.ShowTimer

typealias Action = TimerButtonsActions

enum class TimerButtonsActions {
    NONE,
    ADD,
    CONTROL,
    DELETE;

    var selectedTimer: ShowTimer? = null

    fun passTimer(timer: ShowTimer?) {
        selectedTimer = timer
    }
}