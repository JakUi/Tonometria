package com.klyschenko.tonometria.domain.entity

data class Record(
    val recordId: Int = (0..999_999).random(),
    val day: Int,
    val month: Int,
    val year: Int = 2026,
    val upperPressure: Int,
    val lowerPressure: Int,
    val pulse: Int,
    val wroteAt: DayPart,
    val comment: String?
)
