package com.sdu.izdesdu.domain.usecase

import com.sdu.izdesdu.domain.model.UserProgress
import com.sdu.izdesdu.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.StateFlow

class GetUserProgressUseCase(
    private val repository: ProgressRepository
) {
    operator fun invoke(): StateFlow<UserProgress> = repository.progress
}