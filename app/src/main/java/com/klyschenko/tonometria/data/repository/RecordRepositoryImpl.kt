@file:OptIn(ExperimentalCoroutinesApi::class)

package com.klyschenko.tonometria.data.repository

import com.klyschenko.tonometria.data.db.dao.RecordsDao
import com.klyschenko.tonometria.data.mapper.toDayDataEntity
import com.klyschenko.tonometria.domain.repository.RecordsRepository
import com.klyschenko.tonometria.domain.repository.ToUpdate
import com.klyschenko.tonometria.data.mapper.toDbModel
import com.klyschenko.tonometria.domain.entity.DayData
import com.klyschenko.tonometria.domain.entity.DayPart
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
    private fun emptyParts(): Map<DayPart, List<PressureData>> =
        DayPart.entries.associateWith { emptyList<PressureData>() }

    override fun getAllMonthRecords(
        year: Int,
        month: Int
    ): Flow<Map<Int, Map<DayPart, List<PressureData>>>> {

        val byDay: Flow<Map<Int, List<DayData>>> =
            recordsDao.getAllDays(year, month)
                .flatMapLatest { days ->
                    if (days.isEmpty()) {
                        flowOf(emptyMap())
                    } else {
                        combine(
                            days.map { day ->
                                recordsDao.getDayRecords(year, month, day)
                                    .map { dbList -> dbList.toDayDataEntity() }
                                    .map { list -> day to list }
                            }
                        ) { pairs -> pairs.toMap() }
                    }
                }

        return byDay.map { dayMap ->
            dayMap.mapValues { (_, list) ->
                val grouped: Map<DayPart, List<PressureData>> =
                    list.groupBy { it.wroteAt }
                        .mapValues { (_, items) -> items.map { it.data } }

                // гарантируем MORNING/DAY/EVENING:
                emptyParts() + grouped
            }
        }
    }

    override suspend fun addNewRecord(record: Record) {
        recordsDao.addNewRecord(record.toDbModel())
    }

    override suspend fun editRecord(
        year: Int,
        month: Int,
        day: Int,
        wroteAt: DayPart,
        toUpdate: ToUpdate
    ) {
        recordsDao.editRecord(year, month, day, wroteAt, toUpdate)
    }

    override suspend fun deleteRecord(
        year: Int,
        month: Int,
        day: Int,
        wroteAt: DayPart
    ) {
        recordsDao.deleteRecord(year, month, day, wroteAt)
    }

    override suspend fun addCommentToRecord(
        year: Int,
        month: Int,
        day: Int,
        wroteAt: DayPart,
        comment: String
    ) {
        recordsDao.addCommentToRecord(year, month, day, wroteAt, comment)
    }
}