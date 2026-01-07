package com.klyschenko.tonometria.domain.usecase

import com.klyschenko.tonometria.domain.entity.Record
import com.klyschenko.tonometria.domain.repository.RecordRepository
import javax.inject.Inject

class EditRecordUseCase @Inject constructor(
    private val recordRepository: RecordRepository,
    private val record: Record
) {

    suspend operator fun invoke() = recordRepository.editRecord(record)
}