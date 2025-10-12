package com.example.domain.usecases.quizQuestions

import com.example.data.models.toQuizQuestionEntity
import com.example.data.repositories.quizQuestionRepository.QuizQuestionRepository
import com.example.domain.entities.QuizQuestionEntity

class GetAllQuizQuestionsUseCase(private val quizQuestionRepository: QuizQuestionRepository) {
    suspend operator fun invoke(): Result<List<QuizQuestionEntity>>{
        return try {
            val allQuizQuestionEntities = quizQuestionRepository.getAll().map { it.toQuizQuestionEntity() }
            Result.success(allQuizQuestionEntities)
        } catch (e: Exception){
            Result.failure(e)
        }
    }
}