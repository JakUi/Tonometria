package com.klyschenko.tonometria.domain.usecase

import com.klyschenko.tonometria.domain.repository.RecordRepository
import javax.inject.Inject

class AddCommentToRecordUseCase @Inject constructor(
    private val recordRepository: RecordRepository,
    private val recordId: Int,
    private val comment: String
) {

    suspend operator fun invoke() = recordRepository.addCommentToRecord(recordId, comment)
}