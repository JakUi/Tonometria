package com.klyschenko.tonometria.domain.entity

data class PressureData(
    val upperPressure: Int,
    val lowerPressure: Int,
    val pulse: Int,
    val comment: String?
)