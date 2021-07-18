package com.rsschool.pomodoro.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.rsschool.domain.entity.ShowTimer
import com.rsschool.pomodoro.databinding.ItemTimerBinding

class TimerViewHolder(private val binding: ItemTimerBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(timer: ShowTimer?) {

    }
}

class AddTimerViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    fun bind() {

    }
}