package com.rsschool.pomodoro.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.rsschool.domain.entity.ShowTimer
import com.rsschool.pomodoro.databinding.ItemTimerBinding

class TimersAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var showTimers: List<ShowTimer>? = null

    private var onAddTimerClickListener: OnAddTimerClickListener? = null

    fun setTimers(timers: List<ShowTimer>) {
        showTimers = timers
        notifyDataSetChanged()
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
                        )
                    )
                }
                PLACEHOLDER ->
                    EmptyPlaceholderViewHolder(context.createEmptyPlaceholder())

                else -> AddTimerViewHolder(
                    context.createAddTimerView(),
                    onAddTimerClickListener
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

    fun setOnAddTimerClickListener(listener: OnAddTimerClickListener) {
        onAddTimerClickListener = listener
    }

    private fun Context.createEmptyPlaceholder() = TextView(this)

    private fun Context.createAddTimerView() = MaterialButton(this)

    private companion object {
        const val TIMER = 1
        const val PLACEHOLDER = 2
        const val ADD_TIMER = 3
    }
}