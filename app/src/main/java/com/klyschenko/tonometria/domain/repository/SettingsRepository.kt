package com.klyschenko.tonometria.domain.repository

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    fun getSelectedFontSize(): Flow<Int>

    suspend fun setSelectedFontSize(selectedFontSize: Int)
}