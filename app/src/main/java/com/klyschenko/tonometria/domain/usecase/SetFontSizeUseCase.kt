package com.klyschenko.tonometria.domain.usecase

import com.klyschenko.tonometria.domain.repository.SettingsRepository
import javax.inject.Inject

class SetFontSizeUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {

    suspend operator fun invoke(selectedFontSize: Int) = settingsRepository.setSelectedFontSize(selectedFontSize)
}