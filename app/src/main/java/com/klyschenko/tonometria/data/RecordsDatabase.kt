package com.klyschenko.tonometria.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.klyschenko.tonometria.data.db.converters.DayPartConverter
import com.klyschenko.tonometria.data.db.dao.RecordsDao
import com.klyschenko.tonometria.data.db.entity.RecordsDbModel

@Database(
    entities = [RecordsDbModel::class],
    version = 9,
    exportSchema = false
)
@TypeConverters(DayPartConverter::class)
abstract class RecordsDatabase : RoomDatabase() {

    abstract fun recordsDao(): RecordsDao

    companion object {
        val MIGRATION_5_6 = object : Migration(5, 6) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // если изменений не было
            }
        }

        val MIGRATION_6_7 = object : Migration(6, 7) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // если изменений не было
            }
        }

        val MIGRATION_7_8 = object : Migration(7, 8) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("DROP INDEX IF EXISTS index_records_recordId")

                val cursor = db.query("PRAGMA table_info(records)")
                var commentColorExists = false

                cursor.use {
                    val nameIndex = it.getColumnIndex("name")
                    while (it.moveToNext()) {
                        if (it.getString(nameIndex) == "commentColor") {
                            commentColorExists = true
                            break
                        }
                    }
                }

                if (!commentColorExists) {
                    db.execSQL("ALTER TABLE records ADD COLUMN commentColor INTEGER")
                }

                db.execSQL(
                    """
            DELETE FROM records
            WHERE recordId NOT IN (
                SELECT MAX(recordId)
                FROM records
                GROUP BY year, month, day, wroteAt
            )
            """.trimIndent()
                )

                db.execSQL(
                    """
            CREATE UNIQUE INDEX IF NOT EXISTS index_records_year_month_day_wroteAt
            ON records(year, month, day, wroteAt)
            """.trimIndent()
                )
            }
        }

        val MIGRATION_8_9 = object : Migration(8, 9) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("DROP INDEX IF EXISTS index_records_recordId")

                db.execSQL(
                    """
            CREATE UNIQUE INDEX IF NOT EXISTS index_records_year_month_day_wroteAt
            ON records(year, month, day, wroteAt)
            """.trimIndent()
                )
            }
        }
    }
}