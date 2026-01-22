package com.klyschenko.tonometria.domain.repository

import kotlinx.coroutines.flow.Flow

interface DateRepository {

    fun getSelectedYear(): Flow<Int>

    suspend fun setSelectedYear(selectedYear: Int)

    fun getSelectedMonth(): Flow<Int>

    suspend fun setSelectedMonth(selectedMonth: Int)
}