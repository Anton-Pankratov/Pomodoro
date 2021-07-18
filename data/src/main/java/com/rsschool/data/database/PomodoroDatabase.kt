package com.rsschool.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rsschool.data.entity.StoredTimer

@Database(entities = [StoredTimer::class], version = 1, exportSchema = true)
abstract class PomodoroDatabase : RoomDatabase() {
    abstract fun pomodoroDao(): PomodoroDao
}