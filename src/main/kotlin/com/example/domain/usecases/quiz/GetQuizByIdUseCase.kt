package com.example.domain.usecases.quiz

import com.example.data.models.toQuizEntity
import com.example.data.repositories.quizRpository.QuizRepository
import com.example.domain.entities.QuizEntity

class GetQuizByIdUseCase(private val quizRepository: QuizRepository) {
    suspend operator fun invoke(quizId: Int): Result<QuizEntity> {
        val searchingQuizResult =
            quizRepository.getById(quizId) ?: return Result.failure(Exception("No quiz with id $quizId."))

        return Result.success(searchingQuizResult.toQuizEntity())
    }
}