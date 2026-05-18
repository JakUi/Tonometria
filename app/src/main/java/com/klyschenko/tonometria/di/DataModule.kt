package com.klyschenko.tonometria.di

import android.content.Context
import androidx.room.Room
import com.klyschenko.tonometria.data.db.dao.RecordsDao
import com.klyschenko.tonometria.data.RecordsDatabase
import com.klyschenko.tonometria.data.RecordsDatabase.Companion.MIGRATION_5_6
import com.klyschenko.tonometria.data.RecordsDatabase.Companion.MIGRATION_6_7
import com.klyschenko.tonometria.data.RecordsDatabase.Companion.MIGRATION_7_8
import com.klyschenko.tonometria.data.RecordsDatabase.Companion.MIGRATION_8_9
import com.klyschenko.tonometria.data.repository.DateRepositoryImpl
import com.klyschenko.tonometria.data.repository.RecordsRepositoryImpl
import com.klyschenko.tonometria.data.repository.SettingsRepositoryImpl
import com.klyschenko.tonometria.domain.repository.DateRepository
import com.klyschenko.tonometria.domain.repository.RecordsRepository
import com.klyschenko.tonometria.domain.repository.SettingsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {


    @Binds
    @Singleton
    fun bindRecordsRepository(
        impl: RecordsRepositoryImpl
    ): RecordsRepository

    @Binds
    @Singleton
    fun bindDateRepository(
        impl: DateRepositoryImpl
    ): DateRepository

    @Binds
    @Singleton
    fun bindSettingsRepository(
        impl: SettingsRepositoryImpl
    ): SettingsRepository

    companion object {
        @Singleton
        @Provides
        fun provideRecordsDatabase(
            @ApplicationContext context: Context
        ): RecordsDatabase {
            return Room.databaseBuilder(
                context = context,
                klass = RecordsDatabase::class.java,
                name = "records.db"
            )
                .addMigrations(MIGRATION_5_6)
                .addMigrations(MIGRATION_6_7)
                .addMigrations(MIGRATION_7_8)
                .addMigrations(MIGRATION_8_9)
                .build()
        }

        @Singleton
        @Provides
        fun provideRecordsDao(
            database: RecordsDatabase
        ): RecordsDao = database.recordsDao()
    }
}
