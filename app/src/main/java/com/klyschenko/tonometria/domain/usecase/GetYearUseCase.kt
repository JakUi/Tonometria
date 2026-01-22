package com.klyschenko.tonometria.domain.usecase

import com.klyschenko.tonometria.domain.repository.DateRepository
import javax.inject.Inject

class GetYearUseCase @Inject constructor(
    private val dateRepository: DateRepository
) {

    operator fun invoke() = dateRepository.getSelectedYear()
}