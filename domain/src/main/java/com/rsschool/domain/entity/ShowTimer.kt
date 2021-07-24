package com.rsschool.domain.entity

data class ShowTimer(
    val id: Int? = null,
    val hours: Int?,
    val minutes: Int?,
    val seconds: Int?,
    val state: String?
)