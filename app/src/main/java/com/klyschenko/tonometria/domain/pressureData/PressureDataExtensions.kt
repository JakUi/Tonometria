package com.klyschenko.tonometria.domain.pressureData

import com.klyschenko.tonometria.domain.entity.PressureData

fun List<PressureData>?.valueOf(type: DataType): String {
    val item = this?.firstOrNull()

    return when (type) {
        DataType.UPPER -> item?.upperPressure?.toString() ?: "-"
        DataType.LOWER -> item?.lowerPressure?.toString() ?: "-"
        DataType.PULSE -> item?.pulse?.toString() ?: "-"
    }
}

fun List<PressureData>?.colorValue(): Int? {
    return this?.firstOrNull()?.commentColor
}

enum class DataType {
    UPPER,
    LOWER,
    PULSE
}
