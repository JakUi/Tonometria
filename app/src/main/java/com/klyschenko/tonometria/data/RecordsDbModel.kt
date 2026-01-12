package com.klyschenko.tonometria.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.klyschenko.tonometria.domain.entity.DayPart

@Entity(tableName = "records",
    indices = [Index("recordId")]
)
data class RecordsDbModel(
    @PrimaryKey(autoGenerate = true) val recordId: Int = 0,
    val year: Int,
    val month: Int,
    val day: Int,
    val wroteAt: DayPart,
    val upperPressure: Int,
    val lowerPressure: Int,
    val pulse: Int,
    val comment: String?
)

data class PressureDataDbModel(
    val upperPressure: Int,
    val lowerPressure: Int,
    val pulse: Int,
    val comment: String?
)