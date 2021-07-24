package com.rsschool.domain.usecases

import com.rsschool.domain.entity.ShowTimer

class UpdateTimerUseCase : BaseUseCase() {
    suspend operator fun invoke(timer: ShowTimer) = repository.updateTimer(timer)
}