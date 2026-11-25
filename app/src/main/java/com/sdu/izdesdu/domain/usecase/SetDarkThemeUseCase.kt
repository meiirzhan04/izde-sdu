package com.sdu.izdesdu.domain.usecase

import com.sdu.izdesdu.domain.repository.SettingsRepository

class SetDarkThemeUseCase(private val repository: SettingsRepository) {
    suspend operator fun invoke(enabled: Boolean) = repository.setDarkTheme(enabled)
}
