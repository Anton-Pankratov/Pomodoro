package com.rsschool.data.repository

import com.rsschool.data.database.PomodoroDao
import com.rsschool.domain.entity.ShowTimer
import com.rsschool.data.mapper.TimerMapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class DataSourceImpl(
    private val dao: PomodoroDao,
    private val coroutineDispatcher: CoroutineDispatcher,
    private val mapper: TimerMapper
) : DataSource {

    override val timersFlow: Flow<List<ShowTimer>>
        get() = dao.timersFlow().map { timers ->
            timers.map {
                mapper.toShowTimer(it) }
        }

    override suspend fun saveTimer(timer: ShowTimer) {
        withContext(coroutineDispatcher) {
            dao.saveTimer(mapper.toStoredTimer(timer))
        }
    }

    override suspend fun updateTimer(timer: ShowTimer) {
        withContext(coroutineDispatcher) {
            dao.updateTimer(mapper.toStoredTimer(timer))
        }
    }

    override suspend fun deleteTimer(timer: ShowTimer) {
        withContext(coroutineDispatcher) {
            dao.deleteTimer(mapper.toStoredTimer(timer))
        }
    }

    override suspend fun updateAllTimers() {
        withContext(coroutineDispatcher) {
            dao.updateAllTimers()
        }
    }
}