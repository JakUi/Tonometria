package com.klyschenko.tonometria.data.db.model

import androidx.room.Embedded
import com.klyschenko.tonometria.domain.entity.DayPart

data class DayDataDbModel(
    val day: Int,
    val wroteAt: DayPart,
    @Embedded
    val data: PressureDataDbModel
)
