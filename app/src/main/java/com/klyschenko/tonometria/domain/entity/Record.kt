package com.klyschenko.tonometria.domain.entity

data class PressureData(
    val upperPressure: Int,
    val lowerPressure: Int,
    val pulse: Int,
    val comment: String?
)

data class Record(
    val year: Int,
    val month: Int,
    val day: Int,
    val wroteAt: DayPart,
    val data: PressureData
)