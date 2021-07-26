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

    init {
        calculateTimes()
    }

    fun withStartTime(): ShowTimer {
        startHour = hours
        startMin = minutes
        startSec = seconds
        calculateLeftSeconds()
        calculateCommonSeconds()
        return this
    }

    private fun calculateTimes() {
        if (startSec != null) {
            calculateLeftSeconds()
            calculateCommonSeconds()
        }
    }

    private fun calculateLeftSeconds() {
        calculatedLeftTime = calculate(
            hours ?: 0, minutes ?: 0, seconds ?: 0
        )
    }

    private fun calculateCommonSeconds() {
        calculatedCommonTime = calculate(
            startHour ?: 0, startMin ?: 0, startSec ?: 0
        )
    }

    private fun calculate(h: Int, m: Int, s: Int): Int {
        var calculated = 0
        if (s != 0) calculated += s
        if (m != 0) calculated += (m * 60)
        if (h != 0) calculated += (h * 60 * 60)
        return calculated
    }
}