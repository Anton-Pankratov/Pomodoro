package com.rsschool.pomodoro.presentation.adapter

import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.rsschool.domain.entity.ShowTimer
import com.rsschool.pomodoro.R
import com.rsschool.pomodoro.databinding.ItemTimerBinding
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
        timer?.apply {
            setTimeText()
            setTimeIndication()
            setTimeProgress()
            setOnControlButtonTitle()
            setOnControlBtnClick()
            setOnDeleteBtnClick()
            setTimerItemBackground()
        }
    }

    private fun ShowTimer.setTimeText() {
        binding.timeText.text = setFormatTime()
    }

    private fun ShowTimer.setTimeIndication() {
        binding.indicatorIcon.apply {
            when (state) {
                State.LAUNCHED.name,
                State.RESUMED.name -> startBlink()
                else -> cancelBlink()
            }
        }
    }

    private fun ShowTimer.setTimeProgress() {
        launch(progressCoroutineContext) {
            binding.progressView.withActualTime(
                calculatedLeftTime to calculatedCommonTime
            )
        }
    }

    private fun ShowTimer.setOnControlButtonTitle() {
        binding.timerControlBtn.apply {
            text = resources.getString(
                when (state) {
                    State.CREATED.name -> R.string.button_start
                    State.LAUNCHED.name -> R.string.button_pause
                    State.STOPED.name -> R.string.button_resume
                    State.RESUMED.name -> R.string.button_pause
                    else -> R.string.button_restart
                }
            )
        }
    }

    private fun ShowTimer.setOnDeleteBtnClick() {
        binding.deleteTimerBtn.setOnClickListener {
            listener?.onDeleteClick(this)
        }
    }

    private fun ShowTimer.setOnControlBtnClick() {
        binding.timerControlBtn.setOnClickListener {
            launch(coroutineContext) {
                listener?.onControlClick(this@setOnControlBtnClick)
            }
        }
    }

    private fun ShowTimer.setTimerItemBackground() {
        binding.root.apply {
            background = ContextCompat.getDrawable(
                context, if (calculatedLeftTime == 0) {
                    R.drawable.bg_item_finish_timer
                } else {
                    R.drawable.bg_item_bottom_edge
                }
            )
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
        text = resources.getString(R.string.placeholder_no_timers)
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

    fun View.setLayoutParametersWithMargins(withTop: Boolean = false) {
        layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            context.apply {
                val padding = toDp(16)
                setMargins(padding, if (withTop) padding else 0, padding, 0)
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