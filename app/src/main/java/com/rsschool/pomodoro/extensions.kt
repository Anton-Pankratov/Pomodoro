package com.rsschool.pomodoro

import android.content.Context
import androidx.annotation.StringRes
import timber.log.Timber
import kotlin.math.roundToInt

fun debug(messageLog: String) = Timber.d(messageLog)

fun Context.getStringResource(@StringRes stringRes: Int) =
    resources.getString(stringRes)

fun Context.toDp(value: Int) =
    (value * resources.displayMetrics.density + 0.5f).roundToInt()
