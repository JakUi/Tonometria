package com.klyschenko.tonometria.domain.pressureData

import com.klyschenko.tonometria.domain.entity.PressureData

fun List<PressureData>?.valueOf(type: DataType): String =
    this?.firstOrNull()?.let {
        when (type) {
            DataType.UPPER -> it.upperPressure.toString()
            DataType.LOWER -> it.lowerPressure.toString()
            DataType.PULSE -> it.pulse.toString()
        }
    } ?: "-"

enum class DataType {
    UPPER,
    LOWER,
    PULSE
}