package com.klyschenko.tonometria.domain.usecase

import com.klyschenko.tonometria.domain.repository.RecordsRepository
import com.klyschenko.tonometria.domain.repository.ToUpdate
import javax.inject.Inject

class EditRecordUseCase @Inject constructor(
    private val recordsRepository: RecordsRepository,
    private val recordId: Int,
    private val toUpdate: ToUpdate
) {

    suspend operator fun invoke() = recordsRepository.editRecord(recordId, toUpdate)
}