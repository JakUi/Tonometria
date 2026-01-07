package com.klyschenko.tonometria.domain.usecase

import com.klyschenko.tonometria.domain.repository.RecordsRepository
import javax.inject.Inject

class GetAllMonthsRecordsUseCase @Inject constructor(
    private val recordsRepository: RecordsRepository,
    private val year: Int,
    private val month: Int
) {

    operator fun invoke() = recordsRepository.getAllMonthRecords(year, month)
}