package com.klyschenko.tonometria.domain.usecase

import com.klyschenko.tonometria.domain.repository.RecordsRepository
import javax.inject.Inject

class AddCommentToRecordUseCase @Inject constructor(
    private val recordsRepository: RecordsRepository,
) {

    suspend operator fun invoke(recordId: Int, comment: String) = recordsRepository.addCommentToRecord(recordId, comment)
}