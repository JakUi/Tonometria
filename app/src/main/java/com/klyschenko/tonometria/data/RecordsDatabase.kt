package com.klyschenko.tonometria.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [RecordsDbModel::class],
    version = 1,
    exportSchema = false
)
abstract class RecordsDatabase: RoomDatabase() {

    abstract fun recordsDao(): RecordsDao
}
