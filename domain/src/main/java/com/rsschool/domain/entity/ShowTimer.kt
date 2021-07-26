package com.rsschool.domain.entity

data class ShowTimer(
    val id: Int? = null,
    var hours: Int?,
    var minutes: Int?,
    var seconds: Int?,
    var state: String?,
    var startHour: Int? = 0,
    var startMin: Int? = 0,
    var startSec: Int? = 0,
    var calculatedLeftTime: Int? = 0,
    var calculatedCommonTime: Int? = 0
) {

    fun withStartTime(): ShowTimer {
        startHour = hours
        startMin = minutes
        startSec = seconds
        calculateLeftSeconds()
        calculateCommonSeconds()
        return this
    }

    private fun calculateLeftSeconds() {
        calculatedLeftTime = calculate(startHour, startMin, startSec)
    }

    private fun calculateCommonSeconds() {
        calculatedCommonTime =  calculate(startHour, startMin, startSec)
    }

    private fun calculate(h: Int?, m: Int?, s: Int?): Int? {
        return s?.times(
            if (m != null && m != 0) m else 1
        )?.times(
            if (h != null && h != 0) h else 1
        )
    }
}