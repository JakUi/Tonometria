package com.klyschenko.tonometria.domain.usecase

import com.klyschenko.tonometria.domain.repository.DateRepository
import javax.inject.Inject

class SetMonthUseCase @Inject constructor(
    private val dateRepository: DateRepository
) {

    suspend operator fun invoke(month: Int) = dateRepository.setSelectedMonth(month)
}