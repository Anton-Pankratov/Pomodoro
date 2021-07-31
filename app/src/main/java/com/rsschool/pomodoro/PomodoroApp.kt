package com.rsschool.pomodoro

import android.app.Application
import android.content.Intent
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.rsschool.data.di.DataModule
import com.rsschool.domain.di.UseCasesModule
import com.rsschool.pomodoro.di.ViewModelModule
import com.rsschool.pomodoro.service.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class PomodoroApp : Application(), LifecycleObserver {

    override fun onCreate() {
        super.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        startKoin {
            androidContext(this@PomodoroApp)
            modules(
                DataModule,
                UseCasesModule,
                ViewModelModule
            )
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private fun appOnBackground() {
        with(Intent(this, PomodoroService::class.java)) {
            putExtra(COMMAND_ID, Command.START.value)
            putExtra(TIMER_ID, launchedTimerId)
            startService(this)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private fun appOnForeground() {
        with(Intent(this, PomodoroService::class.java)) {
            putExtra(COMMAND_ID, Command.STOP.value)
            startService(this)
        }
    }
}