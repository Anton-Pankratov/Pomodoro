package com.rsschool.domain.di

import com.rsschool.domain.usecases.DeleteTimerUseCase
import com.rsschool.domain.usecases.GetTimersUseCase
import com.rsschool.domain.usecases.SaveTimerUseCase
import org.koin.dsl.module

val UseCasesModule = module {

    single { GetTimersUseCase() }

    single { SaveTimerUseCase() }

    single { DeleteTimerUseCase() }

}