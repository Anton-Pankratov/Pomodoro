package com.rsschool.pomodoro.presentation.timerDialog

import com.rsschool.pomodoro.entities.SelectTimeEntity
import com.rsschool.pomodoro.presentation.base.BaseViewModel

class TimerPickerDialogViewModel : BaseViewModel() {

    val selectTime
        get() = selectedTime

    fun keepSelectTime(time: SelectTimeEntity) {
        selectedTime = time
    }
}