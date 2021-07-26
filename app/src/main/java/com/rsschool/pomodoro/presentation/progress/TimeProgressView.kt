package com.rsschool.pomodoro.presentation.progress

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.shapes.Shape
import android.util.AttributeSet
import android.view.View
import androidx.annotation.AttrRes

class TimeProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    @AttrRes defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var timeLeft: Int = 0
    private var timeCommon: Int = 0

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (timeLeft == 0 || timeCommon == 0) return
        canvas?.drawProgressCircle()
    }

    fun withActualTime(time: Pair<Int?, Int?>?) {
        time?.apply {
            timeLeft = first ?: 0
            timeCommon = second ?: 0
            invalidate()
        }
    }

    private fun Canvas.drawProgressCircle() =
        drawArc(0f, 0f, width.toFloat(),
            height.toFloat(), -90f, calculateCurrentAngle(),
            true, formCyclePaint())

    private fun calculateCurrentAngle() =
        ((timeCommon - timeLeft).toFloat() / timeCommon.toFloat()) * 360

    private fun formCyclePaint(): Paint {
        return Paint().apply {
            /** flame_magenta -> "#FF617C" */
            color = Color.parseColor("#FF617C")
            style = Paint.Style.FILL
            strokeWidth = 3f
        }
    }
}