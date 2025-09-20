package com.example.domain.usecases.quiz

import com.example.data.models.toQuizEntity
import com.example.data.repositories.quizRpository.QuizRepository
import com.example.domain.entities.QuizEntity

class GetAllQuizzesUseCase(private val quizRepository: QuizRepository) {
    suspend operator fun invoke(): Result<List<QuizEntity>> {
        return try {
            val results = quizRepository.getAll()
            Result.success(results.map { it.toQuizEntity() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}