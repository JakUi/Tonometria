package com.klyschenko.tonometria.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.klyschenko.tonometria.domain.repository.DateRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "date")

class DateRepositoryImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
) : DateRepository {

    private val yearKey = intPreferencesKey("year")
    private val monthKey = intPreferencesKey("month")

    override fun getSelectedYear(): Flow<Int> {
        return context.dataStore.data.map { preferences ->
            val year = preferences[yearKey] ?: 2026
            year
        }
    }

    override suspend fun setSelectedYear(selectedYear: Int) {
        context.dataStore.edit { preferences ->
            preferences[yearKey] = selectedYear
        }
    }

    override fun getSelectedMonth(): Flow<Int> {
        return context.dataStore.data.map { preferences ->
            val month = preferences[monthKey] ?: 1
            month
        }
    }

    override suspend fun setSelectedMonth(selectedMonth: Int) {
        context.dataStore.edit { preferences ->
            preferences[monthKey] = selectedMonth
        }
    }
}