package com.rsschool.pomodoro.presentation.adapter

import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.rsschool.domain.entity.ShowTimer
import com.rsschool.pomodoro.R
import com.rsschool.pomodoro.databinding.ItemTimerBinding
import com.rsschool.pomodoro.utils.getStringResource
import com.rsschool.pomodoro.utils.setFormatTime
import com.rsschool.pomodoro.utils.toDp

class TimerViewHolder(
    private val binding: ItemTimerBinding,
    private val listener: OnButtonsClickListener?
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(timer: ShowTimer?) {
        binding.apply {
            timeText.setTime(timer)
            timerOnOffBtn.setOnControlBtnClick(timer)
            deleteTimerBtn.setOnDeleteBtnClick(timer)
            indicatorIcon
            progressView
        }
    }

    private fun TextView.setTime(timer: ShowTimer?) {
        text = timer?.setFormatTime()
    }

    private fun ImageView.setOnControlBtnClick(timer: ShowTimer?) {
        setOnClickListener {
            listener?.onControlClick(timer)
        }
    }

    private fun ImageView.setOnDeleteBtnClick(timer: ShowTimer?) {
        setOnClickListener {
            listener?.onDeleteClick(timer)
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