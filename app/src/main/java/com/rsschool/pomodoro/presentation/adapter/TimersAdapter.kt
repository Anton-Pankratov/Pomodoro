package com.rsschool.pomodoro.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.rsschool.domain.entity.ShowTimer
import com.rsschool.pomodoro.databinding.ItemTimerBinding

class TimersAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var showTimers: List<ShowTimer>? = null

    private var onButtonsClickListener: OnButtonsClickListener? = null

    private var diffUtilCallback: TimersDiffUtilsCallback? = null
    private var diffResult: DiffUtil.DiffResult? = null

    fun setTimers(timers: List<ShowTimer>) {
        diffUtilCallback = showTimers?.let { TimersDiffUtilsCallback(it, timers) }
        diffResult = diffUtilCallback?.let { DiffUtil.calculateDiff(it) }
        diffResult?.dispatchUpdatesTo(this)
        showTimers = timers
        notifyDataSetChanged()
    }

    fun setOnButtonsClickListener(listener: OnButtonsClickListener) {
        onButtonsClickListener = listener
    }

    override fun getItemCount() = showTimers?.size ?: 0

    override fun getItemViewType(position: Int): Int {
        return when (showTimers?.get(position)?.id) {
            -1 -> PLACEHOLDER
            -2 -> ADD_TIMER
            else -> TIMER
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        parent.context.let { context ->
            return when (viewType) {
                TIMER -> {
                    TimerViewHolder(
                        ItemTimerBinding.inflate(
                            LayoutInflater.from(context), parent, false
                        ), onButtonsClickListener
                    )
                }
                PLACEHOLDER -> {
                    EmptyPlaceholderViewHolder(context.createEmptyPlaceholder())
                }
                else -> AddTimerViewHolder(
                    context.createAddTimerView(),
                    onButtonsClickListener
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            TIMER -> (holder as TimerViewHolder).bind(showTimers?.get(position))
            PLACEHOLDER -> (holder as EmptyPlaceholderViewHolder).bind()
            else -> (holder as AddTimerViewHolder).bind()
        }
    }

    private fun Context.createEmptyPlaceholder() = TextView(this)

    private fun Context.createAddTimerView() = MaterialButton(this)

    private companion object {
        const val TIMER = 1
        const val PLACEHOLDER = 2
        const val ADD_TIMER = 3
    }
}