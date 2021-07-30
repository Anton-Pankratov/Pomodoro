package com.rsschool.pomodoro.di

import com.rsschool.pomodoro.presentation.PomodoroViewModel
import com.rsschool.pomodoro.presentation.timerDialog.TimePickerDialogFragment
import com.rsschool.pomodoro.presentation.timerDialog.TimerPickerDialogViewModel
import com.rsschool.pomodoro.service.PomodoroNotificationUtils
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val ViewModelModule = module {

    single { PomodoroNotificationUtils(get()) }

    fragment { TimePickerDialogFragment() }

    viewModel { PomodoroViewModel(get(), get(), get(), get(), get()) }
    viewModel { TimerPickerDialogViewModel() }
}