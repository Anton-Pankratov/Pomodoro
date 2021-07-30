package com.rsschool.pomodoro.presentation

import androidx.lifecycle.asLiveData
import com.rsschool.domain.entity.ShowTimer
import com.rsschool.domain.usecases.*
import com.rsschool.pomodoro.presentation.base.BaseViewModel
import com.rsschool.pomodoro.utils.Action
import com.rsschool.pomodoro.utils.State
import com.rsschool.pomodoro.utils.TimerButtonsActions
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map

class PomodoroViewModel(
    private val getTimersFlowUseCase: GetTimersFlowUseCase,
    private val saveTimerUseCase: SaveTimerUseCase,
    private val updateTimerUseCase: UpdateTimerUseCase,
    private val deleteTimerUseCase: DeleteTimerUseCase,
    private val updateBeforeCloseAppUseCase: UpdateBeforeCloseAppUseCase
) : BaseViewModel() {

    val timersFlow
        get() = getTimersFlowUseCase.invoke().map { list ->
            list.toMutableList().apply {
                if (isNullOrEmpty())
                    addEmptyTimerForView(-1)
                addEmptyTimerForView(-2)
            }.toList().also {
                it.findLaunchedTimer()
            }
        }.asLiveData()

    private val _timerControlFlow = MutableStateFlow(Action.NONE)
    private val timerControlFlow: StateFlow<TimerButtonsActions> = _timerControlFlow

    private var timerLauncherJob: Job? = null

    private var launchedTimer: ShowTimer? = null

    init {
        collectTimerButtonsEvents()
    }

    suspend fun setOnPauseAllTimersBeforeCloseApp() {
        if (timerLauncherJob?.isActive == true) timerLauncherJob?.cancelAndJoin()
        launch { updateBeforeCloseAppUseCase.invoke() }.start()
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
                        Action.NONE -> { }
                    }
                }
                _timerControlFlow.emit(Action.NONE)
            }
        }
    }

    private fun List<ShowTimer>.findLaunchedTimer() {
        launch {
            launchedTimer = this@findLaunchedTimer.find {
                it.state == State.LAUNCHED.name || it.state == State.RESUMED.name
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

    private fun ShowTimer.launchFinishedTimer(updatedState: String) {
        formTimerModel(
            hours = startHour,
            minutes = startMin,
            seconds = startSec
        ).launchTimer(updatedState)
    }

    private fun ShowTimer.launchTimer(newState: String) {
        if (timerLauncherJob?.isActive == true)
            timerLauncherJob?.cancel()
        startRunningTimer(updateTimerState(newState))
    }

    private suspend fun ShowTimer.cancelTimer(newState: String) {
        timerLauncherJob?.cancelAndJoin()
        if (timerLauncherJob?.isActive == false)
            updateTimerUseCase.invoke(updateTimerState(newState))
    }

    /**
     *  TODO check for responsibility of Start/Stop Button
     *  (with Thread button more responsibility, delay - less)
     */
    private fun startRunningTimer(timer: ShowTimer) {
        timerLauncherJob = launch {
            timer.apply {
                pauseOtherLaunchedOrResumedTimer()
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
    }

    private suspend fun ShowTimer.pauseOtherLaunchedOrResumedTimer() {
        if (id != launchedTimer?.id) {
            launchedTimer?.let {
                updateTimerUseCase.invoke(
                    it.updateTimerState(State.PAUSED.name)
                )
            }
        }
    }

    private fun ShowTimer.checkEndOfSeconds(seconds: Int) {
        launch(coroutineContext) {
            if (seconds == 0) {
                if (checkEndOfTime()) {
                    updateTimerUseCase.invoke(
                        updateTimerState(State.FINISHED.name)
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

    private fun ShowTimer.updateTimerState(state: String) =
        formTimerModel(state = state)

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