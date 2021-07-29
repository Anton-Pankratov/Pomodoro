package com.rsschool.pomodoro.presentation

import androidx.lifecycle.asLiveData
import com.rsschool.domain.entity.ShowTimer
import com.rsschool.domain.usecases.DeleteTimerUseCase
import com.rsschool.domain.usecases.GetTimersUseCase
import com.rsschool.domain.usecases.SaveTimerUseCase
import com.rsschool.domain.usecases.UpdateTimerUseCase
import com.rsschool.pomodoro.presentation.base.BaseViewModel
import com.rsschool.pomodoro.utils.Action
import com.rsschool.pomodoro.utils.State
import com.rsschool.pomodoro.utils.TimerButtonsActions
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
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

    private val _timerControlFlow = MutableStateFlow(Action.NONE)
    private val timerControlFlow: StateFlow<TimerButtonsActions> = _timerControlFlow

    private var timerLauncherJob: Job? = null

    init {
        collectTimerButtonsEvents()
    }

    fun setButtonAction(action: Action) =
        launch { _timerControlFlow.emit(action) }

    private fun collectTimerButtonsEvents() {
        launch {
            timerControlFlow.collect { action ->
                action.apply {
                    when (this) {
                        Action.ADD -> saveTimer()
                        Action.DELETE -> deleteTimer(selectedTimer)
                        Action.CONTROL -> setControlActionFor(selectedTimer)
                    }
                }
                _timerControlFlow.emit(Action.NONE)
            }
        }
    }

    private fun saveTimer() =
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

    private fun deleteTimer(timer: ShowTimer?) =
        launch {
            if (timer != null) {
                deleteTimerUseCase.invoke(timer)
            }
        }

    private fun setControlActionFor(timer: ShowTimer?) {
        launch(coroutineContext) {
            timer?.apply {

                when (state) {
                    State.CREATED.name -> launchTimer(State.LAUNCHED.name)

                    State.LAUNCHED.name -> cancelTimer(State.PAUSED.name)

                    State.PAUSED.name -> launchTimer(State.RESUMED.name)

                    State.RESUMED.name -> cancelTimer(State.PAUSED.name)

                    State.FINISHED.name -> launchFinishedTimer(State.LAUNCHED.name)
                }
            }
        }
    }

    private suspend fun ShowTimer.launchFinishedTimer(updatedState: String) {
        formTimerModel(
            hours = startHour,
            minutes = startMin,
            seconds = startSec
        ).launchTimer(updatedState)
    }

    private suspend fun ShowTimer.launchTimer(updatedState: String) {
        timerLauncherJob?.cancel()
        formTimerModel(state = updatedState).apply {
            timerLauncherJob = launch { startRunningTimer(this@apply) }
        }
    }

    private suspend fun ShowTimer.cancelTimer(updatedState: String) {
        timerLauncherJob?.cancel()
        updateTimerUseCase.invoke(formTimerModel(state = updatedState))
    }

    /**
     *  TODO check for responsibility of Start/Stop Button
     *  (with Thread button more responsibility, delay - less)
     */
    private suspend fun startRunningTimer(timer: ShowTimer) {
        timer.apply {
            seconds?.downTo(0)?.forEach { second ->
                formTimerModel(seconds = second).apply {
                    updateTimerUseCase.invoke(this)
                    checkEndOfSeconds(second)
                }
                Thread.sleep(1000)
                // delay(1000)
            }
        }
    }

    private fun ShowTimer.checkEndOfSeconds(seconds: Int) {
        launch(coroutineContext) {
            if (seconds == 0) {
                if (checkEndOfTime()) {
                    updateTimerUseCase.invoke(
                        formTimerModel(state = State.FINISHED.name)
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
        startRunningTimer(newTimerCycle)
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
        add(
            ShowTimer(
                id, null, null, null,
                null, null, null, null,
                null, null
            )
        )
    }
}