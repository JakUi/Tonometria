package com.klyschenko.tonometria.domain.usecase

import com.klyschenko.tonometria.domain.repository.DateRepository
import javax.inject.Inject

class SetYearUseCase @Inject constructor(
    private val dateRepository: DateRepository
) {

    suspend operator fun invoke(year: Int) = dateRepository.setSelectedYear(year)
}