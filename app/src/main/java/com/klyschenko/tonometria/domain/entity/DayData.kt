package com.klyschenko.tonometria.domain.entity

data class DayData(
    val day: Int,
    val wroteAt: DayPart,
    val data: PressureData
)
