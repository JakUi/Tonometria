package com.klyschenko.tonometria.domain.usecase

import com.klyschenko.tonometria.domain.entity.DayPart
import com.klyschenko.tonometria.domain.repository.RecordsRepository
import com.klyschenko.tonometria.domain.repository.ToUpdate
import javax.inject.Inject

class EditRecordUseCase @Inject constructor(
    private val recordsRepository: RecordsRepository,
) {

    suspend operator fun invoke(
        year: Int,
        month: Int,
        day: Int,
        wroteAt: DayPart,
        toUpdate: ToUpdate
    ) = recordsRepository.editRecord(year, month, day, wroteAt, toUpdate)
}