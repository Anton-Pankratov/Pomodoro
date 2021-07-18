package com.rsschool.domain.repository

import com.rsschool.domain.entity.ShowTimer
import kotlinx.coroutines.flow.Flow

interface PomodoroRepository {

    val timers: Flow<List<ShowTimer>>

    suspend fun saveTimer(timer: ShowTimer)

    suspend fun deleteTimer(timer: ShowTimer)
}