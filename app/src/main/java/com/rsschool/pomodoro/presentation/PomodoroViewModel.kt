package com.rsschool.pomodoro.presentation

import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.rsschool.domain.entity.ShowTimer
import com.rsschool.domain.usecases.DeleteTimerUseCase
import com.rsschool.domain.usecases.GetTimersUseCase
import com.rsschool.domain.usecases.SaveTimerUseCase
import com.rsschool.domain.usecases.UpdateTimerUseCase
import com.rsschool.pomodoro.presentation.base.BaseViewModel
import com.rsschool.pomodoro.utils.TimerStates
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class PomodoroViewModel(
    private val getTimersUseCase: GetTimersUseCase,
    private val saveTimerUseCase: SaveTimerUseCase,
    private val updateTimerUseCase: UpdateTimerUseCase,
    private val deleteTimerUseCase: DeleteTimerUseCase
) : BaseViewModel() {

    val timersFlow
        get() = getTimersUseCase.invoke().map { list ->
            list.toMutableList().apply {
                if (list.isNullOrEmpty())
                    add(ShowTimer(-1, null, null, null, null))
                add(ShowTimer(-2, null, null, null, null))
            }.toList()
        }.asLiveData()

    fun saveTimer() =
        viewModelScope.launch {
            saveTimerUseCase.invoke(
                ShowTimer(
                    hours = selectedTime.hours,
                    minutes = selectedTime.minutes,
                    seconds = selectedTime.seconds,
                    state = TimerStates.CREATED.name
                )
            )
        }

    fun updateTimer(timer: ShowTimer) =
        viewModelScope.launch {
            timer.seconds?.downTo(0)?.forEach { second ->
                updateTimerUseCase.invoke(
                    ShowTimer(
                        timer.id,
                        timer.hours,
                        timer.minutes,
                        second,
                        TimerStates.LAUNCHED.name
                    )
                )
                delay(1000)
            }
        }

    fun deleteTimer(timer: ShowTimer?) =
        viewModelScope.launch {
            if (timer != null) {
                deleteTimerUseCase.invoke(timer)
            }
        }
}