package com.klyschenko.tonometria.domain.repository

import com.klyschenko.tonometria.domain.entity.Record
import kotlinx.coroutines.flow.Flow
import java.time.Month

interface RecordRepository {

    fun getAllMonthRecords(year: Int, month: Month): Flow<List<Record>>

    suspend fun addNewRecord(record: Record)

    suspend fun editRecord(record: Record)

    suspend fun deleteRecord(recordId: Int)

    suspend fun addComment(recordId: Int)
}