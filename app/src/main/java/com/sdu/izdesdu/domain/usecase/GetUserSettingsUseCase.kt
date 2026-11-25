package com.sdu.izdesdu.domain.usecase

import com.sdu.izdesdu.domain.repository.SettingsRepository

class GetUserSettingsUseCase(private val repository: SettingsRepository) {
    operator fun invoke() = repository.settings
}
