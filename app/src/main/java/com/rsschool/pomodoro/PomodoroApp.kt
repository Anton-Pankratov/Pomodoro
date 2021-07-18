package com.rsschool.pomodoro

import android.app.Application
import com.rsschool.data.di.DataModule
import com.rsschool.domain.di.UseCasesModule
import com.rsschool.pomodoro.di.ViewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class PomodoroApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@PomodoroApp)
            modules(
                DataModule, UseCasesModule, ViewModelModule
            )
        }
    }
}