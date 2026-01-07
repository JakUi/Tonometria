package com.klyschenko.tonometria.domain.usecase

import com.klyschenko.tonometria.domain.repository.RecordsRepository
import javax.inject.Inject

class AddCommentToRecordUseCase @Inject constructor(
    private val recordsRepository: RecordsRepository,
    private val recordId: Int,
    private val comment: String
) {

    suspend operator fun invoke() = recordsRepository.addCommentToRecord(recordId, comment)
}