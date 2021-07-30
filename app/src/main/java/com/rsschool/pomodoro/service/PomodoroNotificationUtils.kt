package com.rsschool.pomodoro.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.rsschool.pomodoro.R
import com.rsschool.pomodoro.presentation.PomodoroActivity

class PomodoroNotificationUtils(val context: Context) {

    private val notificationBuilder by lazy { build() }

    fun getNotification(content: String) =
        notificationBuilder.setContentText(content).build()

    fun provide(context: Context) =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager

    private fun build() = NotificationCompat.Builder(context, CHANNEL_ID)
        .setContentTitle(TITLE)
        .setGroup(TIMERS_GROUP)
        .setGroupSummary(false)
        .setDefaults(NotificationCompat.DEFAULT_ALL)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setContentIntent(formNotificationPendingIntent())
        .setSilent(true)
        .setSmallIcon(R.drawable.ic_timer_on_indicator)

    fun createChannel(manager: NotificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = "pomodoro"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val notificationChannel = NotificationChannel(
                CHANNEL_ID, channelName, importance
            )
            manager.createNotificationChannel(notificationChannel)
        }
    }

    private fun formNotificationPendingIntent(): PendingIntent? {
        val resultIntent = Intent(context, PomodoroActivity::class.java)
        resultIntent.flags =
            Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        return PendingIntent.getActivity(
            context, 0, resultIntent, PendingIntent.FLAG_CANCEL_CURRENT
        )
    }

    companion object {
        const val TITLE = "Pomodoro"
        const val CHANNEL_ID = "channel_id"
        const val TIMERS_GROUP = "timers"
    }
}