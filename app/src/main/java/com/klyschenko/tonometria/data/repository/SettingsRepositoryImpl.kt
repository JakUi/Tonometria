package com.klyschenko.tonometria.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.klyschenko.tonometria.domain.repository.SettingsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val Context.fontSizeStore: DataStore<Preferences> by preferencesDataStore(name = "fontSize")

class SettingsRepositoryImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
) : SettingsRepository {

    private val fontSizeKey = intPreferencesKey("size")

    override fun getSelectedFontSize(): Flow<Int> {
        return context.fontSizeStore.data.map { preferences ->
            val fontSize = preferences[fontSizeKey] ?: 12
            fontSize
        }
    }

    override suspend fun setSelectedFontSize(selectedFontSize: Int) {
        context.fontSizeStore.edit { preferences ->
            preferences[fontSizeKey] = selectedFontSize
        }
    }
}