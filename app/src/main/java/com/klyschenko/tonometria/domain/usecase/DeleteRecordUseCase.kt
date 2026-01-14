package com.klyschenko.tonometria.domain.usecase

import com.klyschenko.tonometria.domain.entity.DayPart
import com.klyschenko.tonometria.domain.repository.RecordsRepository
import javax.inject.Inject
import kotlin.Int

class DeleteRecordUseCase @Inject constructor(
    private val recordsRepository: RecordsRepository,
) {

    suspend operator fun invoke(year: Int, month: Int, day: Int, wroteAt: DayPart) = recordsRepository.deleteRecord(year, month, day, wroteAt)
}