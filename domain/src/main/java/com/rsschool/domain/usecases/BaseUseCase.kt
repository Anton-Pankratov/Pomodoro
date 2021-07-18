package com.rsschool.domain.usecases

import com.rsschool.domain.repository.PomodoroRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

abstract class BaseUseCase : KoinComponent {

    protected val repository: PomodoroRepository by inject()
}