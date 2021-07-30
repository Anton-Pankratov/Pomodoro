package com.rsschool.data.repository

import com.rsschool.domain.entity.ShowTimer
import com.rsschool.domain.repository.PomodoroRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf

class PomodoroRepositoryImpl : PomodoroRepository, KoinComponent {

    private val repositoryDispatcher = Dispatchers.IO

    private val dataSource: DataSource by inject {
        parametersOf(repositoryDispatcher)
    }

    override val timers: Flow<List<ShowTimer>>
        get() = dataSource.timersFlow

    override fun timer(id: Int?) = dataSource.timerFlow(id)


    override suspend fun saveTimer(timer: ShowTimer) =
        dataSource.saveTimer(timer)

    override suspend fun updateTimer(timer: ShowTimer) =
        dataSource.updateTimer(timer)

    override suspend fun deleteTimer(timer: ShowTimer) =
        dataSource.deleteTimer(timer)

    override suspend fun updateAllTimers() =
        dataSource.updateAllTimers()
}