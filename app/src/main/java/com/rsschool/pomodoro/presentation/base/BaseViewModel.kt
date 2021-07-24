package com.rsschool.pomodoro.presentation.base

import androidx.lifecycle.ViewModel
import com.rsschool.pomodoro.entities.SelectTimeEntity
import com.rsschool.pomodoro.utils.TimeUnits
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel : ViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

    companion object {
        var selectedTime = SelectTimeEntity(
            TimeUnits.HOUR.default,
            TimeUnits.MINUTE.default,
            TimeUnits.SECOND.default
        )
    }
}