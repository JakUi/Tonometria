package com.klyschenko.tonometria.domain.repository

import com.klyschenko.tonometria.domain.entity.Record
import kotlinx.coroutines.flow.Flow

interface RecordsRepository {

    fun getAllMonthRecords(year: Int, month: Int): Flow<List<Record>>

    suspend fun addNewRecord(record: Record)

    suspend fun editRecord(recordId: Int, toUpdate: ToUpdate)

    suspend fun deleteRecord(recordId: Int)

    suspend fun addCommentToRecord(recordId: Int, comment: String)
}

sealed class ToUpdate {
    data class UpperPressure(val upperPressure: Int) : ToUpdate()
    data class LowerPressure(val lowerPressure: Int) : ToUpdate()
    data class Pulse(val pulse: Int) : ToUpdate()
    data class Comment(val comment: String) : ToUpdate()
}