package com.klyschenko.tonometria.domain.entity

data class Record(
    val year: Int,
    val month: Int,
    val day: Int,
    val wroteAt: DayPart,
    val data: PressureData,
    val comment: String = ""
)