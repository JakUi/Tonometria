package com.klyschenko.tonometria.domain.entity

import java.time.Month

data class Record(
    val recordId: Int = (0..999_999).random(),
    val date: Int,
    val month: Month,
    val year: Int,
    val upperPressure: Int,
    val lowerPressure: Int,
    val pulse: Int,
    val wroteAt: DayPart,
    val comment: String?
)
