package com.klyschenko.tonometria.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.klyschenko.tonometria.data.db.model.DayDataDbModel
import com.klyschenko.tonometria.data.db.entity.RecordsDbModel
import com.klyschenko.tonometria.domain.repository.ToUpdate
import kotlinx.coroutines.flow.Flow

@Dao
interface RecordsDao {

    @Query("SELECT DISTINCT day FROM records WHERE year=:year AND month=:month ORDER BY day")
    fun getAllDays(year: Int, month: Int): Flow<List<Int>>

    @Query("SELECT day, wroteAt, upperPressure, lowerPressure, pulse, comment FROM records WHERE year=:year AND month=:month AND day=:day")
    fun getDayRecords(year: Int, month: Int, day: Int): Flow<List<DayDataDbModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNewRecord(recordsDbModel: RecordsDbModel)

    @Query("DELETE FROM records WHERE recordId=:recordId")
    suspend fun deleteRecord(recordId: Int)

    @Query("UPDATE records SET upperPressure=:upperPressure WHERE recordId=:recordId")
    suspend fun updateUpperPressure(recordId: Int, upperPressure: Int)

    @Query("UPDATE records SET lowerPressure=:lowerPressure WHERE recordId=:recordId")
    suspend fun updateLowerPressure(recordId: Int, lowerPressure: Int)

    @Query("UPDATE records SET pulse=:pulse WHERE recordId=:recordId")
    suspend fun updatePulse(recordId: Int, pulse: Int)

    suspend fun editRecord(recordId: Int, toUpdate: ToUpdate) {
        when(toUpdate) {
            is ToUpdate.Comment -> addCommentToRecord(recordId, toUpdate.comment)
            is ToUpdate.LowerPressure -> updateLowerPressure(recordId, toUpdate.lowerPressure)
            is ToUpdate.Pulse -> updatePulse(recordId, toUpdate.pulse)
            is ToUpdate.UpperPressure -> updateUpperPressure(recordId, toUpdate.upperPressure)
        }
    }

    @Query("UPDATE records SET comment=:comment WHERE recordId=:recordId")
    suspend fun addCommentToRecord(recordId: Int, comment: String)
}