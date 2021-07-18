package com.rsschool.data.mapper

import com.rsschool.domain.entity.ShowTimer
import com.rsschool.data.entity.StoredTimer

class TimerMapper {

    fun toShowTimer(stored: StoredTimer): ShowTimer {
        stored.apply {
            return ShowTimer(id, time)
        }
    }

    fun toStoredTimer(show: ShowTimer): StoredTimer {
        show.apply {
            return StoredTimer(id, time)
        }
    }
}