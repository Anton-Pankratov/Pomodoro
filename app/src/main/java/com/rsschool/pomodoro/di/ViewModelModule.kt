package com.rsschool.pomodoro.di

import com.rsschool.pomodoro.presentation.PomodoroViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val ViewModelModule = module {

    viewModel { PomodoroViewModel(get(), get(), get()) }
}