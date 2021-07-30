package com.rsschool.pomodoro

import android.app.Application
import android.content.Intent
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.rsschool.data.di.DataModule
import com.rsschool.domain.di.UseCasesModule
import com.rsschool.domain.usecases.UpdateBeforeCloseAppUseCase
import com.rsschool.pomodoro.di.ViewModelModule
import com.rsschool.pomodoro.service.PomodoroService
import com.rsschool.pomodoro.utils.debug
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.fragment.koin.fragmentFactory
import org.koin.core.KoinExperimentalAPI
import org.koin.core.context.startKoin
import timber.log.Timber

class PomodoroApp : Application(), LifecycleObserver {

    override fun onCreate() {
        super.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
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

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private fun appOnBackground() {
        val startIntent = Intent(this, PomodoroService::class.java)
        startIntent.putExtra("COMMAND_ID", "COMMAND_START")
        startIntent.putExtra("STARTED_TIMER_TIME_MS", "startTime")
        startService(startIntent)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private fun appOnForeground() {
        val stopIntent = Intent(this, PomodoroService::class.java)
        stopIntent.putExtra("COMMAND_ID", "COMMAND_START")
        startService(stopIntent)
    }
}