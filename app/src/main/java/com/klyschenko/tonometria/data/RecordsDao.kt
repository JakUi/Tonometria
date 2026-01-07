package com.klyschenko.tonometria.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.klyschenko.tonometria.domain.repository.ToUpdate
import kotlinx.coroutines.flow.Flow
import java.time.Month

@Dao
interface RecordsDao {

    @Query("SELECT * FROM records WHERE year==:year AND month==:month")
    fun getAllMonthRecords(year: Int, month: Month): Flow<List<RecordsDbModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNewRecord(recordsDbModel: RecordsDbModel)

    @Query("DELETE FROM records WHERE recordId == :recordId")
    fun deleteRecord(recordId: Int)

    @Query("UPDATE records SET upperPressure=:upperPressure WHERE recordId==:recordId")
    fun updateUpperPressure(recordId: Int, upperPressure: Int)

    @Query("UPDATE records SET lowerPressure=:lowerPressure WHERE recordId==:recordId")
    fun updateLowerPressure(recordId: Int, lowerPressure: Int)

    @Query("UPDATE records SET pulse=:pulse WHERE recordId==:recordId")
    fun updatePulse(recordId: Int, pulse: Int)

    suspend fun editRecord(recordId: Int, toUpdate: ToUpdate) {
        when(toUpdate) {
            is ToUpdate.Comment -> addCommentToRecord(recordId, toUpdate.comment)
            is ToUpdate.LowerPressure -> updateLowerPressure(recordId, toUpdate.lowerPressure)
            is ToUpdate.Pulse -> updatePulse(recordId, toUpdate.pulse)
            is ToUpdate.UpperPressure -> updateUpperPressure(recordId, toUpdate.upperPressure)
        }
    }

    @Query("UPDATE records SET comment=:comment WHERE recordId==:recordId")
    suspend fun addCommentToRecord(recordId: Int, comment: String)
}