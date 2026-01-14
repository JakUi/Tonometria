package com.klyschenko.tonometria.domain.usecase

import com.klyschenko.tonometria.domain.entity.DayPart
import com.klyschenko.tonometria.domain.repository.RecordsRepository
import javax.inject.Inject

class AddCommentToRecordUseCase @Inject constructor(
    private val recordsRepository: RecordsRepository,
) {

    suspend operator fun invoke(
        year: Int,
        month: Int,
        day: Int,
        wroteAt: DayPart,
        comment: String
    ) = recordsRepository.addCommentToRecord(year, month, day, wroteAt, comment)
}