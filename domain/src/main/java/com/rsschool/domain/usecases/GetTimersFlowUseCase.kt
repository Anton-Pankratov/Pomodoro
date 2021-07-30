package com.rsschool.domain.usecases

class GetTimersFlowUseCase : BaseUseCase() {
    operator fun invoke() = repository.timers
}