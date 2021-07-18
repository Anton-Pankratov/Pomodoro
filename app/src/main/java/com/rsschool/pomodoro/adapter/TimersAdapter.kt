package com.rsschool.pomodoro.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rsschool.domain.entity.ShowTimer
import com.rsschool.pomodoro.databinding.ItemTimerBinding

class TimersAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var _binding: ItemTimerBinding? = null
    private val binding get() = _binding

    private var showTimers: List<ShowTimer>? = null

    fun setTimers(timers: List<ShowTimer>) {
        showTimers = timers
        notifyDataSetChanged()
    }

    override fun getItemCount() = showTimers?.size ?: 0

    override fun getItemViewType(position: Int): Int {
        return if (position == showTimers?.size?.minus(1))
            ADD_TIMER else TIMER
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == TIMER) {
            ItemTimerBinding.inflate(LayoutInflater.from(parent.context), parent, false).apply {
                _binding = this
                return TimerViewHolder(this)
            }
        } else {
            return AddTimerViewHolder(View(parent.context))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            TIMER -> (holder as TimerViewHolder).bind(showTimers?.get(position))
            else -> (holder as AddTimerViewHolder).bind()
        }
    }

    private companion object {
        const val TIMER = 1
        const val ADD_TIMER = 2
    }
}