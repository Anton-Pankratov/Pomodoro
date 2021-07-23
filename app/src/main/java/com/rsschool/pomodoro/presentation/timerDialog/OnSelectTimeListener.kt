package com.rsschool.pomodoro.presentation.timerDialog

import com.rsschool.pomodoro.entities.SelectTimeEntity

interface OnSelectTimeListener {

    fun setTime(time: SelectTimeEntity)
}