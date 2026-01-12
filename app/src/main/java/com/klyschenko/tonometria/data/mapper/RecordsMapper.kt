package com.klyschenko.tonometria.data.mapper

import com.klyschenko.tonometria.data.db.model.DayDataDbModel
import com.klyschenko.tonometria.data.db.entity.RecordsDbModel
import com.klyschenko.tonometria.data.db.model.PressureDataDbModel
import com.klyschenko.tonometria.domain.entity.DayData
import com.klyschenko.tonometria.domain.entity.PressureData
import com.klyschenko.tonometria.domain.entity.Record
import kotlin.Int

//fun List<RecordsDbModel>.toEntities(): List<Record> {
//    return map {
//        Record(
//            year = it.year,
//            month = it.month,
//            day = it.day,
//            wroteAt = it.wroteAt,
//            data = PressureData(it.upperPressure, it.lowerPressure, it.pulse, it.comment)
//        )
//    }.distinct()
//}

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

fun List<DayDataDbModel>.toPressureData(): List<PressureData> {
    return map { dayDataDbModel ->
        PressureData(
            upperPressure = dayDataDbModel.data.upperPressure,
            lowerPressure = dayDataDbModel.data.lowerPressure,
            pulse = dayDataDbModel.data.pulse,
            comment = dayDataDbModel.data.comment
        )
    }
}

fun PressureDataDbModel.toEntity(): PressureData =
    PressureData(
        upperPressure = upperPressure,
        lowerPressure = lowerPressure,
        pulse = pulse,
        comment = comment
    )

fun DayDataDbModel.toEntityDayData(year: Int, month: Int): DayData =
    DayData(
        day = day,
        wroteAt = wroteAt,
        data = data.toEntity()
    )