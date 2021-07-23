package com.rsschool.pomodoro.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.rsschool.domain.entity.ShowTimer

class TimersDiffUtilsCallback(
    private val oldList: List<ShowTimer>,
    private val newList: List<ShowTimer>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}