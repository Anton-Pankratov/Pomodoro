package com.rsschool.pomodoro.presentation

import androidx.lifecycle.asLiveData
import com.rsschool.domain.entity.ShowTimer
import com.rsschool.domain.usecases.DeleteTimerUseCase
import com.rsschool.domain.usecases.GetTimersUseCase
import com.rsschool.domain.usecases.SaveTimerUseCase
import com.rsschool.domain.usecases.UpdateTimerUseCase
import com.rsschool.pomodoro.presentation.base.BaseViewModel
import com.rsschool.pomodoro.utils.State
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
                    addEmptyTimerForView(-1)
                addEmptyTimerForView(-2)
            }.toList()
        }.asLiveData()

    fun saveTimer() =
        launch {
            saveTimerUseCase.invoke(
                ShowTimer(
                    hours = selectedTime.hours,
                    minutes = selectedTime.minutes,
                    seconds = selectedTime.seconds,
                    state = State.CREATED.name,
                ).withStartTime()
            )
        }

    fun deleteTimer(timer: ShowTimer?) =
        launch {
            if (timer != null) {
                deleteTimerUseCase.invoke(timer)
            }
        }

    private var countdownIsPaused = false

    fun setControlActionFor(timer: ShowTimer) {
        launch(coroutineContext) {
            var controlTimer: ShowTimer
            timer.apply {
                when (state) {
                    State.CREATED.name -> {
                        countdownIsPaused = false
                        controlTimer = formTimerModel(state = State.LAUNCHED.name)
                        launchTimer(controlTimer)
                        updateTimerUseCase.invoke(controlTimer)
                    }
                    State.LAUNCHED.name -> {
                        countdownIsPaused = true
                        controlTimer = formTimerModel(state = State.PAUSED.name)
                        updateTimerUseCase.invoke(controlTimer)
                    }
                    State.PAUSED.name -> {
                        countdownIsPaused = false
                        controlTimer = formTimerModel(state = State.RESUMED.name)
                        launchTimer(controlTimer)
                        updateTimerUseCase.invoke(controlTimer)
                    }
                    State.RESUMED.name -> {
                        countdownIsPaused = true
                        controlTimer = formTimerModel(state = State.PAUSED.name)
                        updateTimerUseCase.invoke(controlTimer)
                    }
                    State.FINISHED.name -> {
                        countdownIsPaused = false
                        launchTimer(
                            formTimerModel(
                                hours = startHour,
                                minutes = startMin,
                                seconds = startSec,
                                state = State.LAUNCHED.name
                            )
                        )
                    }
                }
            }
        }
    }

    private fun launchTimer(timer: ShowTimer) {
        timer.apply {
            launch(coroutineContext) {
                seconds?.downTo(0)?.forEach { second ->
                    if (countdownIsPaused) {
                        return@launch
                    }
                    formTimerModel(seconds = second).apply {
                        updateTimerUseCase.invoke(this)
                        checkEndOfSeconds(second)
                    }
                    /**
                     *  TODO check for responsibility of Start/Stop Button
                     *  (with Thread button more responsibility, delay - less)
                     */
                    Thread.sleep(1000)
                    // delay(1000)
                }
            }
        }
    }

    private fun ShowTimer.checkEndOfSeconds(seconds: Int) {
        launch(coroutineContext) {
            if (seconds == 0) {
                if (checkEndOfTime()) {
                    updateTimerUseCase.invoke(
                        formTimerModel(
                            state = State.FINISHED.name
                        )
                    )
                    return@launch
                }
                if (checkEndOfMinutes()) {
                    updateNextTimerCycle(
                        formTimerModel(
                            hours = hours?.minus(1),
                            minutes = 59,
                            seconds = 59
                        )
                    )
                } else {
                    updateNextTimerCycle(
                        formTimerModel(
                            minutes = minutes?.minus(1),
                            seconds = 59
                        )
                    )
                }
            }
        }
    }

    private suspend fun ShowTimer.updateNextTimerCycle(newTimerCycle: ShowTimer) {
        updateTimerUseCase.invoke(formTimerModel())
        delay(1000)
        updateTimerUseCase.invoke(newTimerCycle)
        launchTimer(newTimerCycle)
    }

    private fun ShowTimer.checkEndOfMinutes() = minutes == 0

    private fun ShowTimer.checkEndOfTime() =
        (hours ?: 0) + (minutes ?: 0) + (seconds ?: 0) == 0

    private fun ShowTimer.formTimerModel(
        hours: Int? = this.hours,
        minutes: Int? = this.minutes,
        seconds: Int? = this.seconds,
        state: String? = this.state
    ) = ShowTimer(
        id, hours, minutes, seconds,
        state, startHour, startMin, startSec
    )

    private fun MutableList<ShowTimer>.addEmptyTimerForView(id: Int) {
        add(ShowTimer(id, null, null, null,
            null, null, null, null,
            null, null)
        )
    }
}