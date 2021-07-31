package com.rsschool.data.di

import androidx.room.Room
import com.rsschool.data.database.PomodoroDatabase
import com.rsschool.data.mapper.TimerMapper
import com.rsschool.data.repository.DataSource
import com.rsschool.data.repository.DataSourceImpl
import com.rsschool.data.repository.PomodoroRepositoryImpl
import com.rsschool.domain.repository.PomodoroRepository
import kotlinx.coroutines.CoroutineDispatcher
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val DataModule = module {

    single {
        PomodoroDatabase::class.simpleName?.let { dbName ->
            Room.databaseBuilder(
                androidContext().applicationContext,
                PomodoroDatabase::class.java, dbName)
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()
        }
    }

    single { get<PomodoroDatabase>().pomodoroDao() }

    single<DataSource> { (dispatcher: CoroutineDispatcher) ->
        DataSourceImpl(get(), dispatcher, get()) }

    single<PomodoroRepository> { PomodoroRepositoryImpl() }

    single { TimerMapper() }
}