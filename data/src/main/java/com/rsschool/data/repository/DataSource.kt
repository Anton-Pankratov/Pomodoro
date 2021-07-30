package com.rsschool.data.repository

import com.rsschool.domain.entity.ShowTimer
import kotlinx.coroutines.flow.Flow

interface DataSource {

    val timersFlow: Flow<List<ShowTimer>>

    suspend fun saveTimer(timer: ShowTimer)

    suspend fun updateTimer(timer: ShowTimer)

    suspend fun deleteTimer(timer: ShowTimer)

    suspend fun updateAllTimers()
}