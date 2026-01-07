package com.klyschenko.tonometria.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.klyschenko.tonometria.domain.entity.DayPart
import java.time.Month


@Entity(tableName = "records")
data class RecordDbModel(
    @PrimaryKey val recordId: Int,
    val year: Int,
    val month: Month,
    val day: Int,
    val wroteAt: DayPart,
    val upperPressure: Int,
    val lowerPressure: Int,
    val pulse: Int,
    val comment: String
)
