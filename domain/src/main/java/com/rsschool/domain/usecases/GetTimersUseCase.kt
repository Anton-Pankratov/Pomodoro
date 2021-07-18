package com.rsschool.domain.usecases

class GetTimersUseCase : BaseUseCase() {
    operator fun invoke() = repository.timers
}