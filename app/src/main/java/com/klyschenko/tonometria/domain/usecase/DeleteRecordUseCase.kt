package com.klyschenko.tonometria.domain.usecase

import com.klyschenko.tonometria.domain.repository.RecordsRepository
import javax.inject.Inject

class DeleteRecordUseCase @Inject constructor(
    private val recordsRepository: RecordsRepository,
    private val recordId: Int
) {

    suspend operator fun invoke() = recordsRepository.deleteRecord(recordId)
}