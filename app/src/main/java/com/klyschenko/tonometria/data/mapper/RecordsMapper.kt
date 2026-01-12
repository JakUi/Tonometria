package com.klyschenko.tonometria.data.mapper

import com.klyschenko.tonometria.data.PressureDataDbModel
import com.klyschenko.tonometria.data.RecordsDbModel
import com.klyschenko.tonometria.domain.entity.PressureData
import com.klyschenko.tonometria.domain.entity.Record

fun List<RecordsDbModel>.toEntities(): List<Record> {
    return map {
        Record(
            year = it.year,
            month = it.month,
            day = it.day,
            wroteAt = it.wroteAt,
            data = PressureData(it.upperPressure, it.lowerPressure, it.pulse, it.comment)
        )
    }.distinct()
}

fun Record.toDbModel(): RecordsDbModel {
    return RecordsDbModel(
        year = year,
        month = month,
        day = day,
        wroteAt = wroteAt,
        upperPressure = data.upperPressure,
        lowerPressure = data.lowerPressure,
        pulse = data.pulse,
        comment = data.comment ?: ""
    )
}

fun List<PressureDataDbModel>.toPressureData(): List<PressureData> {
    return map {pressureDataDbModel ->
        PressureData(
            upperPressure = pressureDataDbModel.upperPressure,
            lowerPressure = pressureDataDbModel.lowerPressure,
            pulse = pressureDataDbModel.pulse,
            comment = pressureDataDbModel.comment
        )
    }
}
