package com.rsschool.pomodoro

import android.app.Application
import com.rsschool.data.di.DataModule
import com.rsschool.domain.di.UseCasesModule
import com.rsschool.pomodoro.di.ViewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.fragment.koin.fragmentFactory
import org.koin.core.KoinExperimentalAPI
import org.koin.core.context.startKoin
import timber.log.Timber

class PomodoroApp : Application() {

    override fun onCreate() {
        super.onCreate()
        plantTimber()
        startKoin {
            androidContext(this@PomodoroApp)
            modules(
                DataModule, UseCasesModule, ViewModelModule
            )
        }
    }

    private fun plantTimber() {
        Timber.plant(Timber.DebugTree())
    }
}