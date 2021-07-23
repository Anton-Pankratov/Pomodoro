package com.rsschool.pomodoro.presentation.timerDialog

import androidx.lifecycle.ViewModel
import com.rsschool.pomodoro.entities.SelectTimeEntity

class TimerPickerDialogViewModel : ViewModel() {

    val selectTime
        get() = _selectTime

    fun keepSelectTime(time: SelectTimeEntity) {
        _selectTime = time
    }

    private companion object {
        var _selectTime: SelectTimeEntity? = null
    }
}