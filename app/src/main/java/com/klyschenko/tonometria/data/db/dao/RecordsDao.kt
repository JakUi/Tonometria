package com.klyschenko.tonometria.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.klyschenko.tonometria.data.db.model.DayDataDbModel
import com.klyschenko.tonometria.data.db.entity.RecordsDbModel
import com.klyschenko.tonometria.domain.entity.DayPart
import com.klyschenko.tonometria.domain.repository.ToUpdate
import kotlinx.coroutines.flow.Flow

@Dao
interface RecordsDao {

    @Query("SELECT DISTINCT day FROM records WHERE year=:year AND month=:month ORDER BY day")
    fun getAllDays(year: Int, month: Int): Flow<List<Int>>

    @Query("SELECT day, wroteAt, upperPressure, lowerPressure, pulse, comment, commentColor FROM records WHERE year=:year AND month=:month AND day=:day")
    fun getDayRecords(year: Int, month: Int, day: Int): Flow<List<DayDataDbModel>>

    @Query("SELECT day, wroteAt, upperPressure, lowerPressure, pulse, comment, commentColor FROM records WHERE year=:year AND month=:month AND day=:day AND wroteAt=:wroteAt")
    fun getSingleRecord(year: Int, month: Int, day: Int, wroteAt: DayPart): Flow<List<DayDataDbModel>>

//    @Query("SELECT recordId FROM records WHERE year=:year AND month=:month AND day=:day AND wroteAt=:wroteAt")
//    fun getRecordId(year: Int, month: Int, day: Int, wroteAt: DayPart): Int?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNewRecord(recordsDbModel: RecordsDbModel)

    @Query("DELETE FROM records WHERE year=:year AND month=:month AND day=:day AND wroteAt=:wroteAt")
    suspend fun deleteRecord(year: Int, month: Int, day: Int, wroteAt: DayPart)

    @Query("UPDATE records SET upperPressure=:upperPressure WHERE year=:year AND month=:month AND day=:day AND wroteAt=:wroteAt")
    suspend fun updateUpperPressure(year: Int, month: Int, day: Int, wroteAt: DayPart, upperPressure: Int)

    @Query("UPDATE records SET lowerPressure=:lowerPressure WHERE year=:year AND month=:month AND day=:day AND wroteAt=:wroteAt")
    suspend fun updateLowerPressure(year: Int, month: Int, day: Int, wroteAt: DayPart, lowerPressure: Int)

    @Query("UPDATE records SET pulse=:pulse WHERE year=:year AND month=:month AND day=:day AND wroteAt=:wroteAt")
    suspend fun updatePulse(year: Int, month: Int, day: Int, wroteAt: DayPart, pulse: Int)

    suspend fun editRecord(year: Int, month: Int, day: Int, wroteAt: DayPart, toUpdate: ToUpdate) {
        when(toUpdate) {
            is ToUpdate.Comment -> addCommentToRecord(year, month, day, wroteAt, toUpdate.comment)
            is ToUpdate.LowerPressure -> updateLowerPressure(year, month, day, wroteAt, toUpdate.lowerPressure)
            is ToUpdate.Pulse -> updatePulse(year, month, day, wroteAt, toUpdate.pulse)
            is ToUpdate.UpperPressure -> updateUpperPressure(year, month, day, wroteAt, toUpdate.upperPressure)
        }
    }

    @Query("UPDATE records SET comment=:comment WHERE year=:year AND month=:month AND day=:day AND wroteAt=:wroteAt")
    suspend fun addCommentToRecord(year: Int, month: Int, day: Int, wroteAt: DayPart, comment: String)
}