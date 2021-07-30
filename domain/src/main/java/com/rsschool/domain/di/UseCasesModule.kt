package com.rsschool.domain.di

import com.rsschool.domain.usecases.*
import org.koin.dsl.module

val UseCasesModule = module {

    single { GetTimersFlowUseCase() }

    single { SaveTimerUseCase() }

    single { UpdateTimerUseCase() }

    single { DeleteTimerUseCase() }

    single { UpdateBeforeCloseAppUseCase() }

}