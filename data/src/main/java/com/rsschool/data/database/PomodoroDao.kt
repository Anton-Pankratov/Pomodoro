package com.rsschool.data.database

import androidx.room.*
import com.rsschool.data.entity.StoredTimer
import kotlinx.coroutines.flow.Flow

@Dao
interface PomodoroDao {

    @Query("SELECT * FROM timer")
    fun timersFlow(): Flow<List<StoredTimer>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveTimer(timer: StoredTimer)

    @Delete
    suspend fun deleteTimer(timer: StoredTimer)

    @Update(entity = StoredTimer::class)
    suspend fun updateTimer(timer: StoredTimer)

    @Query("UPDATE timer SET state = 'PAUSED' WHERE state LIKE 'RESUMED' OR state LIKE 'LAUNCHED'")
    suspend fun updateAllTimers()
}