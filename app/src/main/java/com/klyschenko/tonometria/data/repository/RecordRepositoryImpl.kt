package com.klyschenko.tonometria.data.repository

import com.klyschenko.tonometria.data.RecordsDao
import com.klyschenko.tonometria.domain.entity.Record
import com.klyschenko.tonometria.domain.repository.RecordsRepository
import com.klyschenko.tonometria.domain.repository.ToUpdate
import com.klyschenko.tonometria.data.mapper.toEntities
import com.klyschenko.tonometria.data.mapper.toDbModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RecordsRepositoryImpl @Inject constructor(
    private val recordsDao: RecordsDao
) : RecordsRepository {
    override fun getAllMonthRecords(
        year: Int,
        month: Int
    ): Flow<List<Record>> {
        return recordsDao.getAllMonthRecords(year, month).map { it.toEntities() }
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