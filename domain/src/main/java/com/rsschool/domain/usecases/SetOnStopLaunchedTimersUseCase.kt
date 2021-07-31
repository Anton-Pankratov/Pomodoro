package com.rsschool.domain.usecases

class SetOnStopLaunchedTimersUseCase : BaseUseCase() {
    suspend operator fun invoke() = repository.updateAllTimers()
}