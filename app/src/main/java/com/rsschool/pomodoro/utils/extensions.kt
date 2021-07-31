package com.rsschool.pomodoro.utils

import android.content.Context
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import androidx.core.view.isVisible
import com.rsschool.domain.entity.ShowTimer
import com.rsschool.pomodoro.entities.SelectTimeEntity
import kotlin.math.roundToInt

fun Context.toDp(value: Int) =
    (value * resources.displayMetrics.density + 0.5f).roundToInt()

fun ShowTimer.setFormatTime() =
    "${hours?.format()} : ${minutes?.format()} : ${seconds?.format()}"

fun SelectTimeEntity.setFormatTime() =
    "${hours.format()}:${minutes.format()}:${seconds.format()}"

fun ImageView.startBlink() {
    isVisible = true
    startAnimation(AlphaAnimation(1f, 0f)
        .apply {
            duration = 700
            interpolator = LinearInterpolator()
            repeatCount = Animation.INFINITE
            repeatMode = Animation.REVERSE
        }
    )
}

fun ImageView.cancelBlink() {
    isVisible = false
    if (animation != null) {
        animation.cancel()
    }
}


private fun Int.format() =
    if (this < 10) "0$this" else "$this"