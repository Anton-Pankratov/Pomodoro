package com.rsschool.domain.usecases

import com.rsschool.domain.entity.ShowTimer

class SaveTimerUseCase : BaseUseCase() {
    suspend operator fun invoke(timer: ShowTimer) = repository.saveTimer(timer)
}