package com.rsschool.pomodoro.presentation.adapter

import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.rsschool.domain.entity.ShowTimer
import com.rsschool.pomodoro.R
import com.rsschool.pomodoro.databinding.ItemTimerBinding
import com.rsschool.pomodoro.presentation.progress.TimeProgressView
import com.rsschool.pomodoro.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class TimerViewHolder(
    private val binding: ItemTimerBinding,
    private val listener: OnButtonsClickListener?
) :
    RecyclerView.ViewHolder(binding.root), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default

    private val progressCoroutineContext: CoroutineContext
        get() = Dispatchers.Default

    fun bind(timer: ShowTimer?) {
        timer?.let {
            binding.apply {
                timeText.setTime(it)
                indicatorIcon.setIndication(it.state)
                progressView.setActualTime(
                    it.calculatedLeftTime to it.calculatedCommonTime
                )
                deleteTimerBtn.setOnDeleteBtnClick(it)
                timerControlBtn.apply {
                    setOnControlBtnClick(it)
                    changeButtonTitle(
                        it.state ?: State.CREATED.name
                    )
                }
            }
        }
    }

    private fun TextView.setTime(timer: ShowTimer?) {
        text = timer?.setFormatTime()
    }

    private fun ImageView.setIndication(state: String?) {
        when (state) {
            State.LAUNCHED.name,
            State.RESUMED.name -> startBlink()
            else -> cancelBlink()
        }
    }

    private fun TimeProgressView.setActualTime(time: Pair<Int?, Int?>?) {
        launch(progressCoroutineContext) { withActualTime(time) }
    }

    private fun MaterialButton.changeButtonTitle(state: String) {
        text = resources.getString(
            when (state) {
                State.CREATED.name -> R.string.button_start
                State.LAUNCHED.name -> R.string.button_pause
                State.PAUSED.name -> R.string.button_resume
                State.RESUMED.name -> R.string.button_pause
                else -> R.string.button_restart
            }
        )
    }

    private fun ImageView.setOnDeleteBtnClick(timer: ShowTimer?) {
        setOnClickListener {
            listener?.onDeleteClick(timer)
        }
    }

    private fun MaterialButton.setOnControlBtnClick(timer: ShowTimer?) {
        setOnClickListener {
            launch(coroutineContext) {
                listener?.onControlClick(timer)
            }
        }
    }
}

class EmptyPlaceholderViewHolder(private val placeholder: TextView) :
    BaseTimerItemViewHolder(placeholder) {

    fun bind() {
        placeholder.apply {
            setLayoutParameters()
            setPaddings()
            setText()
            setCenterGravity()
        }
    }

    private fun TextView.setText() {
        text = context.getStringResource(R.string.text_empty_placeholder)
    }

    private fun TextView.setCenterGravity() {
        gravity = Gravity.CENTER
    }
}

class AddTimerViewHolder(
    private val view: MaterialButton,
    private val clickListener: OnButtonsClickListener?
) : BaseTimerItemViewHolder(view) {

    fun bind() {
        view.apply {
            setOnAddTimerClickListener()
            setLayoutParametersWithMargins()
            setPaddings()
            setBackground()
            setIcon()
        }
    }

    private fun MaterialButton.setOnAddTimerClickListener() {
        setOnClickListener { clickListener?.onAddListener() }
    }

    private fun MaterialButton.setBackground() {
        cornerRadius = context.toDp(6)
    }

    private fun MaterialButton.setIcon() {
        text = "\u2795"
    }
}

abstract class BaseTimerItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    fun View.setLayoutParameters() {
        layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
    }

    fun View.setLayoutParametersWithMargins() {
        layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            context.apply {
                val padding = toDp(16)
                setMargins(padding, 0, padding, 0)
            }

        }
    }

    fun View.setPaddings() {
        context.apply {
            val padding = toDp(16)
            setPadding(padding, padding, padding, padding)
        }
    }
}