package com.klyschenko.tonometria.domain.usecase

import com.klyschenko.tonometria.domain.repository.RecordsRepository
import javax.inject.Inject

class GetAllMonthsRecordsUseCase @Inject constructor(
    private val recordsRepository: RecordsRepository,
) {
    operator fun invoke(year: Int, month: Int) = recordsRepository.getAllMonthRecords(year, month)
}