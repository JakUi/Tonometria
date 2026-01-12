package com.klyschenko.tonometria.domain.usecase

import com.klyschenko.tonometria.domain.entity.DayData
import com.klyschenko.tonometria.domain.repository.RecordsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllMonthsRecordsUseCase @Inject constructor(
    private val recordsRepository: RecordsRepository,
) {
    operator fun invoke(year: Int, month: Int): Flow<Map<Int, List<DayData>>> =
        recordsRepository.getAllMonthRecords(year, month)
}