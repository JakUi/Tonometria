package com.klyschenko.tonometria.domain.repository

import com.klyschenko.tonometria.domain.entity.DayData
import com.klyschenko.tonometria.domain.entity.DayPart
import com.klyschenko.tonometria.domain.entity.PressureData
import com.klyschenko.tonometria.domain.entity.Record
import kotlinx.coroutines.flow.Flow

interface RecordsRepository {

    fun getAllMonthRecords(year: Int, month: Int): Flow<Map<Int, Map<DayPart, List<PressureData>>>>

    suspend fun addNewRecord(record: Record)

    suspend fun getDayRecord(year: Int, month: Int, day: Int, wroteAt: DayPart): DayData

    suspend fun editRecord(year: Int, month: Int, day: Int, wroteAt: DayPart, toUpdate: ToUpdate)

    suspend fun deleteRecord(year: Int, month: Int, day: Int, wroteAt: DayPart)

    suspend fun addCommentToRecord(year: Int, month: Int, day: Int, wroteAt: DayPart, comment: String)
}

sealed class ToUpdate {
    data class UpperPressure(val upperPressure: Int) : ToUpdate()
    data class LowerPressure(val lowerPressure: Int) : ToUpdate()
    data class Pulse(val pulse: Int) : ToUpdate()
    data class Comment(val comment: String) : ToUpdate()
}