package com.rsschool.pomodoro.presentation

import androidx.lifecycle.asLiveData
import com.rsschool.domain.entity.ShowTimer
import com.rsschool.domain.usecases.DeleteTimerUseCase
import com.rsschool.domain.usecases.GetTimersUseCase
import com.rsschool.domain.usecases.SaveTimerUseCase
import com.rsschool.pomodoro.presentation.base.BaseViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class PomodoroViewModel(
    private val getTimersUseCase: GetTimersUseCase,
    private val deleteTimerUseCase: DeleteTimerUseCase,
    private val saveTimerUseCase: SaveTimerUseCase,
) : BaseViewModel() {

    val timersFlow get() = getTimersUseCase.invoke().map { list ->
        list.toMutableList().apply {
            add(ShowTimer(-1, null, null, null))
            add(ShowTimer(-2, null, null, null))
        }.toList()
    }.asLiveData()

    fun saveTimer(timer: ShowTimer) {
        launch {  saveTimerUseCase.invoke(timer) }
    }

}