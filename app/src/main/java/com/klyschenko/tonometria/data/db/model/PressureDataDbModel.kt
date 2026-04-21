package com.klyschenko.tonometria.data.db.model

data class PressureDataDbModel(
    val upperPressure: Int,
    val lowerPressure: Int,
    val pulse: Int,
    val comment: String?,
    val commentColor: Int?
)