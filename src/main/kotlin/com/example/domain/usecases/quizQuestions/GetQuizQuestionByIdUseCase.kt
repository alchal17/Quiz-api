package com.example.domain.usecases.quizQuestions

import com.example.data.models.toQuizQuestionEntity
import com.example.data.repositories.quizQuestionRepository.QuizQuestionRepository
import com.example.domain.entities.QuizQuestionEntity

class GetQuizQuestionByIdUseCase(private val quizQuestionRepository: QuizQuestionRepository) {
    suspend operator fun invoke(quizQuestionId: Int): Result<QuizQuestionEntity> {
        val quizQuestion = quizQuestionRepository.getById(quizQuestionId)
            ?: return Result.failure(Exception("No quiz question found with id $quizQuestionId"))

        return Result.success(quizQuestion.toQuizQuestionEntity())
    }
}