package com.rsschool.data.repository

import com.rsschool.domain.entity.ShowTimer
import com.rsschool.domain.repository.PomodoroRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PomodoroRepositoryImpl : PomodoroRepository, KoinComponent {

    private val dataSource: DataSource by inject()

    override val timers: Flow<List<ShowTimer>>
        get() = dataSource.timersFlow

    override suspend fun saveTimer(timer: ShowTimer) {
        dataSource.saveTimer(timer)
    }

    override suspend fun deleteTimer(timer: ShowTimer) {

    }
}