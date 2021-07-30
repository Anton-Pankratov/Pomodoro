package com.rsschool.domain.repository

import com.rsschool.domain.entity.ShowTimer
import kotlinx.coroutines.flow.Flow

interface PomodoroRepository {

    val timers: Flow<List<ShowTimer>>

    fun timer(id: Int?): Flow<ShowTimer>

    suspend fun saveTimer(timer: ShowTimer)

    suspend fun updateTimer(timer: ShowTimer)

    suspend fun deleteTimer(timer: ShowTimer)

    suspend fun updateAllTimers()
}