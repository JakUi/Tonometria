package com.klyschenko.tonometria.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.klyschenko.tonometria.data.db.converters.DayPartConverter
import com.klyschenko.tonometria.data.db.dao.RecordsDao
import com.klyschenko.tonometria.data.db.entity.RecordsDbModel

@Database(
    entities = [RecordsDbModel::class],
    version = 6,
    exportSchema = false
)
@TypeConverters(DayPartConverter::class)
abstract class RecordsDatabase: RoomDatabase() {

    abstract fun recordsDao(): RecordsDao
}