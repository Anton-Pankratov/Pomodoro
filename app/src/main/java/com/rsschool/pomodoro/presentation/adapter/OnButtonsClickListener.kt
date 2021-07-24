package com.rsschool.pomodoro.presentation.adapter

import com.rsschool.domain.entity.ShowTimer

interface OnButtonsClickListener {

    fun onControlClick(timer: ShowTimer?)

    fun onDeleteClick(timer: ShowTimer?)

    fun onAddListener()

}