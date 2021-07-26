package com.rsschool.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "timer")
data class StoredTimer(
    @PrimaryKey val id: Int?,
    val hours: Int?,
    val minutes: Int?,
    val seconds: Int?,
    val state: String?,
    val startHour: Int?,
    val startMin: Int?,
    val startSec: Int?,
    val calculatedLeftTime: Int?,
    val calculatedCommonTime: Int?
)