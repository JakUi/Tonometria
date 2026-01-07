package com.klyschenko.tonometria.domain.usecase

import com.klyschenko.tonometria.domain.repository.RecordRepository
import com.klyschenko.tonometria.domain.repository.ToUpdate
import javax.inject.Inject

class EditRecordUseCase @Inject constructor(
    private val recordRepository: RecordRepository,
    private val recordId: Int,
    private val toUpdate: ToUpdate
) {

    suspend operator fun invoke() = recordRepository.editRecord(recordId, toUpdate)
}