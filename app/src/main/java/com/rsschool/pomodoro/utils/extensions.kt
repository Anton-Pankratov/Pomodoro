package com.rsschool.pomodoro.utils

import android.content.Context
import androidx.annotation.StringRes
import com.rsschool.domain.entity.ShowTimer
import com.rsschool.pomodoro.entities.SelectTimeEntity
import timber.log.Timber
import kotlin.math.roundToInt

fun debug(messageLog: String) = Timber.d(messageLog)

fun Context.getStringResource(@StringRes stringRes: Int) =
    resources.getString(stringRes)

fun Context.toDp(value: Int) =
    (value * resources.displayMetrics.density + 0.5f).roundToInt()

fun ShowTimer.setFormatTime() =
    "${hours?.format()}:${minutes?.format()}:${seconds?.format()}"

fun SelectTimeEntity.setFormatTime() =
    "${hours.format()}:${minutes.format()}:${seconds.format()}"

private fun Int.format() =
    if (this < 10) "0$this" else "$this"