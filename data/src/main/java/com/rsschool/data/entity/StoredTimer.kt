package com.rsschool.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "timer")
data class StoredTimer(
    @PrimaryKey val id: Int,
    val time: String? = null
)