package com.rsschool.pomodoro.adapter

import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.rsschool.domain.entity.ShowTimer
import com.rsschool.pomodoro.R
import com.rsschool.pomodoro.databinding.ItemTimerBinding

class TimerViewHolder(private val binding: ItemTimerBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(timer: ShowTimer?) {

    }
}

class AddTimerViewHolder(private val view: ImageView) : RecyclerView.ViewHolder(view) {

    fun bind() {
        view.apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            setImageDrawable(
                ContextCompat.getDrawable(
                    context, R.drawable.ic_add_timer
                )
            )
        }
    }
}