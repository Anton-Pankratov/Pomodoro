package com.rsschool.domain.usecases

class GetLaunchedTimerUseCase : BaseUseCase() {
    operator fun invoke(timerId: Int?) = repository.timer(timerId)
}