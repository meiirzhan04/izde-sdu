package com.sdu.izdesdu.domain.usecase

import com.sdu.izdesdu.domain.repository.ProgressRepository

class SaveAnswerProgressUseCase(
    private val repository: ProgressRepository
) {
    operator fun invoke(isCorrect: Boolean, earnedPoints: Int) {
        repository.addAnswerResult(isCorrect, earnedPoints)
    }
}