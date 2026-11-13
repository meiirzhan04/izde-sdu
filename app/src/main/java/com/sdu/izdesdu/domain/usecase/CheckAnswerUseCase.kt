package com.sdu.izdesdu.domain.usecase

import com.sdu.izdesdu.domain.model.AnswerResult
import com.sdu.izdesdu.domain.model.Question

class CheckAnswerUseCase {
    operator fun invoke(question: Question, selectedIndex: Int): AnswerResult {
        val isCorrect = selectedIndex == question.correctIndex
        return AnswerResult(isCorrect, if (isCorrect) POINTS_PER_CORRECT else 0)
    }
    private companion object {
        const val POINTS_PER_CORRECT = 10
    }
}
