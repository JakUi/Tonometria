package com.klyschenko.tonometria.domain.usecase

import com.klyschenko.tonometria.domain.repository.RecordsRepository
import com.klyschenko.tonometria.domain.repository.ToUpdate
import javax.inject.Inject

class EditRecordUseCase @Inject constructor(
    private val recordsRepository: RecordsRepository,
) {

    suspend operator fun invoke(recordId: Int, toUpdate: ToUpdate) = recordsRepository.editRecord(recordId, toUpdate)
}