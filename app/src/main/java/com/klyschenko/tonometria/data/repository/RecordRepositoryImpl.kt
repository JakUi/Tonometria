@file:OptIn(ExperimentalCoroutinesApi::class)

package com.klyschenko.tonometria.data.repository

import com.klyschenko.tonometria.data.db.dao.RecordsDao
import com.klyschenko.tonometria.domain.repository.RecordsRepository
import com.klyschenko.tonometria.domain.repository.ToUpdate
import com.klyschenko.tonometria.data.mapper.toDbModel
import com.klyschenko.tonometria.data.mapper.toPressureData
import com.klyschenko.tonometria.domain.entity.PressureData
import com.klyschenko.tonometria.domain.entity.Record
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RecordsRepositoryImpl @Inject constructor(
    private val recordsDao: RecordsDao
) : RecordsRepository {

    override fun getAllMonthRecords(
        year: Int,
        month: Int
    ): Flow<Map<Int, List<PressureData>>> {

        val byDay: Flow<Map<Int, List<PressureData>>> =
            recordsDao.getAllDays(year, month)
                .flatMapLatest { days ->
                    if (days.isEmpty()) {
                        flowOf(emptyMap())
                    } else {
                        combine(
                            days.map { day ->
                                recordsDao.getDayRecords(year, month, day)
                                    .map { dbList -> dbList.toPressureData() }
                                    .map { list -> day to list }
                            }
                        ) { pairs ->
                            pairs.toMap()
                        }
                    }
                }

        return byDay
    }

    override suspend fun addNewRecord(record: Record) {
        recordsDao.addNewRecord(record.toDbModel())
    }

    override suspend fun editRecord(
        recordId: Int,
        toUpdate: ToUpdate
    ) {
        recordsDao.editRecord(recordId, toUpdate)
    }

    override suspend fun deleteRecord(recordId: Int) {
        recordsDao.deleteRecord(recordId)
    }

    override suspend fun addCommentToRecord(recordId: Int, comment: String) {
        recordsDao.addCommentToRecord(recordId, comment)
    }
}