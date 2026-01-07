package com.klyschenko.tonometria.data.mapper

import com.klyschenko.tonometria.data.RecordsDbModel
import com.klyschenko.tonometria.domain.entity.Record

fun List<RecordsDbModel>.toEntities(): List<Record> {
    return map {
        Record(
            recordId = it.recordId,
            day = it.day,
            month = it.month,
            year = it.year,
            upperPressure = it.upperPressure,
            lowerPressure = it.lowerPressure,
            pulse = it.pulse,
            wroteAt = it.wroteAt,
            comment = it.comment
        )
    }.distinct()
}

fun Record.toDbModel(): RecordsDbModel {
    return RecordsDbModel(
        recordId = recordId,
        year = year,
        month = month,
        day = day,
        wroteAt = wroteAt,
        upperPressure = upperPressure,
        lowerPressure = lowerPressure,
        pulse = pulse,
        comment = comment ?: ""
    )
}
