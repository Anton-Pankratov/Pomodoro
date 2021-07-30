package com.rsschool.pomodoro.service

import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import com.rsschool.domain.entity.ShowTimer
import com.rsschool.domain.usecases.GetLaunchedTimerUseCase
import com.rsschool.pomodoro.R
import com.rsschool.pomodoro.utils.setFormatTime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class PomodoroService : Service() {

    private val getLaunchedTimerUseCase: GetLaunchedTimerUseCase by inject()

    private var isServiceStarted = false

    private val notificationUtils: PomodoroNotificationUtils by inject()

    private var notificationManager: NotificationManager? = null

    private val serviceScope = CoroutineScope(Dispatchers.Main)
    private var serviceJob: Job? = null

    override fun onCreate() {
        super.onCreate()
        notificationManager =
            notificationUtils
                .provide(applicationContext)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        processCommand(intent)
        return START_REDELIVER_INTENT
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun processCommand(intent: Intent?) {
        (intent?.extras?.getString(COMMAND_ID)
            ?: Command.INVALID.value).let { command ->
            when (command) {
                Command.START.value ->
                    commandStart(
                        intent?.extras
                            ?.getInt(TIMER_ID) ?: 0
                    )
                Command.STOP.value -> commandStop()
                Command.INVALID.value -> return
            }
        }
    }

    private fun commandStart(timerId: Int) {
        if (isServiceStarted) return

        try {
            moveToStartedState()
            launchPomodoroService()
            continueTimer(timerId)
        } finally {
            isServiceStarted = true
        }
    }

    private fun commandStop() {
        if (!isServiceStarted) return
        try {
            serviceJob?.cancel()
            stopForeground(true)
            stopSelf()
        } finally {
            isServiceStarted = false
        }
    }

    private fun moveToStartedState() {
        val startIntent = Intent(
            this,
            PomodoroService::class.java
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(startIntent)
        } else {
            startService(startIntent)
        }
    }

    private fun launchPomodoroService() {
        notificationUtils.apply {
            notificationManager?.let {
                createChannel(it)
            }
            startForeground(
                NOTIFICATION_ID,
                getNotification(
                    resources.getString(
                        R.string.notification_timer_not_exist
                    )
                )
            )
        }
    }

    private fun continueTimer(timerId: Int) {
        serviceJob = serviceScope.launch {
            if (timerId != 0) {
                getLaunchedTimerUseCase.invoke(timerId).cancellable()
                    .collect { timer ->
                        timer.setNotificationTimerContent(timerId)
                    }
            }
        }
    }

    private fun ShowTimer.setNotificationTimerContent(timerId: Int) =
        notificationManager?.notify(NOTIFICATION_ID, Notification(timerId))

    private fun ShowTimer.Notification(timerId: Int) =
        notificationUtils.getNotification(
            if (timerId != 0) {
                setFormatTime()
            } else {
                resources.getString(
                    R.string.notification_timer_not_exist
                )
            }
        )
}