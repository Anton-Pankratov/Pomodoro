package com.rsschool.pomodoro.presentation.adapter

import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.rsschool.domain.entity.ShowTimer
import com.rsschool.pomodoro.R
import com.rsschool.pomodoro.databinding.ItemTimerBinding
import com.rsschool.pomodoro.getStringResource
import com.rsschool.pomodoro.toDp

class TimerViewHolder(private val binding: ItemTimerBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(timer: ShowTimer?) {

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
    private val clickListener: OnAddTimerClickListener?
) : BaseTimerItemViewHolder(view) {

    fun bind() {
        view.apply {
            setOnAddTimerClickListener()
            setLayoutParameters()
            setPaddings()
            setBackground()
            setIcon()
        }
    }

    private fun MaterialButton.setOnAddTimerClickListener() {
        setOnClickListener { clickListener?.onClick() }
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

    fun View.setPaddings() {
        context.apply {
            val padding = toDp(16)
            setPadding(padding, padding, padding, padding)
        }
    }
}