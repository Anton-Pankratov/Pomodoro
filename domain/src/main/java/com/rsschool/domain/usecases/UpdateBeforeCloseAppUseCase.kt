package com.rsschool.domain.usecases

class UpdateBeforeCloseAppUseCase : BaseUseCase() {
    suspend operator fun invoke() = repository.updateAllTimers()
}