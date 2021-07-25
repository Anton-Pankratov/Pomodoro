package com.rsschool.domain.entity

data class ShowTimer(
    val id: Int? = null,
    var hours: Int?,
    var minutes: Int?,
    var seconds: Int?,
    var state: String?
)