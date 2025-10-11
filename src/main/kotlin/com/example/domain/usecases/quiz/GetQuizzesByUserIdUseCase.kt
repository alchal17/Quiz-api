package com.example.domain.usecases.quiz

import com.example.data.models.toQuizEntity
import com.example.data.repositories.quizRpository.QuizRepository
import com.example.domain.entities.QuizEntity

class GetQuizzesByUserIdUseCase(
    private val quizRepository: QuizRepository,
) {
    suspend operator fun invoke(userId: Int): Result<List<QuizEntity>> {
        return Result.success(quizRepository.findByUserId(userId).map { it.toQuizEntity() })
    }
}