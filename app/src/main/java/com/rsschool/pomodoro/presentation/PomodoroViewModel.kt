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

    fun deleteTimer(timer: ShowTimer?) =
        viewModelScope.launch {
            if (timer != null) {
                deleteTimerUseCase.invoke(timer)
            }
        }

    fun updateTimer(timer: ShowTimer) =
        viewModelScope.launch {
            timer.seconds?.downTo(0)?.forEach { second ->
                timer.formTimerModel(seconds = second).apply {
                    updateTimerUseCase.invoke(this)
                    checkEndOfSeconds(second)
                }
                delay(1000)
            }
        }

    private fun ShowTimer.checkEndOfSeconds(seconds: Int) {
        viewModelScope.launch {
            if (seconds == 0) {
                if (checkEndOfTime()) return@launch
                if (checkEndOfMinutes()) {
                    updateNextTimerCycle(formTimerModel(
                        hours = hours?.minus(1),
                        minutes = 59,
                        seconds = 59
                    ))
                } else {
                    updateNextTimerCycle(formTimerModel(
                        minutes = minutes?.minus(1),
                        seconds = 59
                    ))
                }
            }
        }
    }

    private suspend fun ShowTimer.updateNextTimerCycle(newTimerCycle: ShowTimer) {
        updateTimerUseCase.invoke(formTimerModel())
        delay(1000)
        updateTimerUseCase.invoke(newTimerCycle)
        updateTimer(newTimerCycle)
    }

    private fun ShowTimer.checkEndOfMinutes() = minutes == 0

    private fun ShowTimer.checkEndOfTime() =
        (hours ?: 0) + (minutes ?: 0) + (seconds ?: 0) == 0

    private fun ShowTimer.formTimerModel(
        hours: Int? = this.hours,
        minutes: Int? = this.minutes,
        seconds: Int? = this.seconds,
    ) = ShowTimer(id, hours, minutes, seconds, state)
}