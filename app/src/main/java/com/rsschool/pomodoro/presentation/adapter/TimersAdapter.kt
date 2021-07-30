package com.rsschool.pomodoro.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.rsschool.domain.entity.ShowTimer
import com.rsschool.pomodoro.databinding.ItemTimerBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class TimersAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val scope = CoroutineScope(Dispatchers.Default)

    var showTimers = mutableListOf<ShowTimer>()

    private var onButtonsClickListener: OnButtonsClickListener? = null

    fun setTimers(timers: List<ShowTimer>) {
        showTimers.apply {
            clear()
            addAll(timers)
            notifyDataSetChanged()
        }
    }

    fun setOnButtonsClickListener(listener: OnButtonsClickListener) {
        onButtonsClickListener = listener
    }

    override fun getItemCount() = showTimers.size

    override fun getItemViewType(position: Int): Int {
        return when (showTimers[position].id) {
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
                        ), onButtonsClickListener,
                    )
                }
                PLACEHOLDER -> {
                    EmptyPlaceholderViewHolder(context.createEmptyPlaceholder())
                }
                else -> AddTimerViewHolder(
                    context.createButton(),
                    onButtonsClickListener
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            TIMER -> (holder as TimerViewHolder).bind(showTimers[position])
            PLACEHOLDER -> (holder as EmptyPlaceholderViewHolder).bind()
            else -> (holder as AddTimerViewHolder).bind()
        }
    }

    private fun Context.createEmptyPlaceholder() = TextView(this)

    private fun Context.createButton() = MaterialButton(this)

    private companion object {
        const val TIMER = 1
        const val PLACEHOLDER = 2
        const val ADD_TIMER = 3
    }
}