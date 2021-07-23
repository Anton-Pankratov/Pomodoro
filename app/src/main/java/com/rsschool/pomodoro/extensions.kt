package com.rsschool.pomodoro

import android.content.Context
import androidx.annotation.StringRes
import com.rsschool.pomodoro.entities.SelectTimeEntity
import timber.log.Timber
import kotlin.math.roundToInt

fun debug(messageLog: String) = Timber.d(messageLog)

fun Context.getStringResource(@StringRes stringRes: Int) =
    resources.getString(stringRes)

fun Context.toDp(value: Int) =
    (value * resources.displayMetrics.density + 0.5f).roundToInt()

fun SelectTimeEntity.setFormatTime() =
    "${hours.format()}:${minutes.format()}:${seconds.format()}"


private fun Int.format() =
    if (this < 10) "0$this" else "$this"