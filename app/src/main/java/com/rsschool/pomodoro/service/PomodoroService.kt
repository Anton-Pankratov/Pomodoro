package com.rsschool.pomodoro.service

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import com.rsschool.domain.usecases.GetTimersFlowUseCase
import com.rsschool.pomodoro.utils.debug
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.inject

class PomodoroService : Service() {

    private val getTimersFlowUseCase: GetTimersFlowUseCase by inject()

    private var isServiceStarted = false

    private val notificationUtils: PomodoroNotificationUtils by inject()

    private var notificationManager: NotificationManager? = null

    private val serviceScope = CoroutineScope(Dispatchers.Main)
    private var serviceJob: Job? = null

    override fun onCreate() {
        super.onCreate()
        setNotificationManager()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        processCommand(intent)
        return START_REDELIVER_INTENT
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
    }

    private fun setNotificationManager() {
        notificationManager =
            applicationContext
                .getSystemService(Context.NOTIFICATION_SERVICE)
                    as? NotificationManager
    }

    private fun processCommand(intent: Intent?) {
        when (intent?.extras?.getString(COMMAND_ID) ?: INVALID) {
            COMMAND_START -> {
                commandStart(
                    intent?.extras?.getLong(STARTED_TIMER_TIME_MS)
                        ?: return
                )
            }
            COMMAND_STOP -> commandStop()
            INVALID -> return
        }
    }

    private fun commandStart(startTime: Long) {
        if (isServiceStarted) return

        try {
            moveToStartedState()
            launchPomodoroService()
            continueTimer(startTime)
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
        val startIntent = Intent(this, PomodoroService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(startIntent)
        } else {
            startService(startIntent)
        }
    }

    private fun launchPomodoroService() {
        notificationUtils.apply {
            notificationManager?.let { createChannel(it) }
            startForeground(NOTIFICATION_ID, getNotification("content"))
        }
    }

    private fun continueTimer(startTime: Long) {
        serviceJob = serviceScope.launch {
            getTimersFlowUseCase.invoke().collect {
                debug(it.toString())
            }

            while (true) {
               /* notificationManager?.notify(
                    NOTIFICATION_ID,
                    *//*notificationUtils.getNotification(
                        System.currentTimeMillis() - startTime
                    )*//*
                )*/
            }
        }
    }

    private companion object {
        const val START_TIME = "00:00:00:00"
        const val INVALID = "INVALID"
        const val COMMAND_START = "COMMAND_START"
        const val COMMAND_STOP = "COMMAND_STOP"
        const val COMMAND_ID = "COMMAND_ID"
        const val STARTED_TIMER_TIME_MS = "STARTED_TIMER_TIME"


        private const val CHANNEL_ID = "Channel_ID"
        private const val NOTIFICATION_ID = 777
        private const val INTERVAL = 1000L
    }
}