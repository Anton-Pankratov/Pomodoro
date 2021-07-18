package com.rsschool.pomodoro.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.rsschool.domain.usecases.DeleteTimerUseCase
import com.rsschool.domain.usecases.GetTimersUseCase
import com.rsschool.domain.usecases.SaveTimerUseCase

class PomodoroViewModel(
    private val getTimersUseCase: GetTimersUseCase,
    private val deleteTimerUseCase: DeleteTimerUseCase,
    private val saveTimerUseCase: SaveTimerUseCase,
) : ViewModel() {

    val timersFlow get() = getTimersUseCase.invoke().asLiveData()

}