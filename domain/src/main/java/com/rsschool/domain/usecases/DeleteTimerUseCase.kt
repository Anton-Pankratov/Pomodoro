package com.rsschool.domain.usecases

import com.rsschool.domain.entity.ShowTimer
import com.rsschool.domain.repository.PomodoroRepository

class DeleteTimerUseCase : BaseUseCase() {
    suspend operator fun invoke(timer: ShowTimer) = repository.deleteTimer(timer)
}