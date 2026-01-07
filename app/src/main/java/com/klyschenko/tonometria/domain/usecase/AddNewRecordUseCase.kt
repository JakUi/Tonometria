package com.klyschenko.tonometria.domain.usecase

import com.klyschenko.tonometria.domain.entity.Record
import com.klyschenko.tonometria.domain.repository.RecordsRepository
import javax.inject.Inject

class AddNewRecordUseCase @Inject constructor(
    private val recordsRepository: RecordsRepository,
    private val record: Record
) {

    suspend operator fun invoke() = recordsRepository.addNewRecord(record)
}