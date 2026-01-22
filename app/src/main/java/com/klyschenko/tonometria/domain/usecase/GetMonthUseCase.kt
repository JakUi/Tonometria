package com.klyschenko.tonometria.domain.usecase

import com.klyschenko.tonometria.domain.repository.DateRepository
import javax.inject.Inject

class GetMonthUseCase @Inject constructor(
    private val dateRepository: DateRepository
) {

    operator fun invoke() = dateRepository.getSelectedMonth()
}