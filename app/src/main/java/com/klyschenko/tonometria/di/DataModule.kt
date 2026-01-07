package com.klyschenko.tonometria.di

import android.content.Context
import androidx.room.Room
import com.klyschenko.tonometria.data.RecordsDao
import com.klyschenko.tonometria.data.RecordsDatabase
import com.klyschenko.tonometria.data.repository.RecordsRepositoryImpl
import com.klyschenko.tonometria.domain.repository.RecordsRepository
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
            ).fallbackToDestructiveMigration(dropAllTables = true).build()
        }

        @Singleton
        @Provides
        fun provideRecordsDao(
            database: RecordsDatabase
        ): RecordsDao = database.recordsDao()
    }
}
