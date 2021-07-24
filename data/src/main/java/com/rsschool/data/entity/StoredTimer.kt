package com.rsschool.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "timer")
data class StoredTimer(
    @PrimaryKey val id: Int?,
    val hours: Int?,
    val minutes: Int?,
    val seconds: Int?,
    val state: String?
)