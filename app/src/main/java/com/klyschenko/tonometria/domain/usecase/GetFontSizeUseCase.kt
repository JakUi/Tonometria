package com.klyschenko.tonometria.domain.usecase

import com.klyschenko.tonometria.domain.repository.SettingsRepository
import javax.inject.Inject

class GetFontSizeUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {

    operator fun invoke() = settingsRepository.getSelectedFontSize()
}