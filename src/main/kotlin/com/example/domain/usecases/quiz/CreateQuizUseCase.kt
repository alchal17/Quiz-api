package com.example.domain.usecases.quiz

import com.example.data.models.toQuizEntity
import com.example.data.repositories.quizRpository.QuizRepository
import com.example.data.repositories.quizUserRepository.QuizUserRepository
import com.example.domain.entities.QuizEntity
import com.example.domain.entities.toQuiz

class CreateQuizUseCase(
    private val quizRepository: QuizRepository,
    private val quizUserRepository: QuizUserRepository
) {
    suspend operator fun invoke(quizEntity: QuizEntity): Result<QuizEntity> {
        return try {
            quizUserRepository.getById(quizEntity.userId)
                ?: return Result.failure(Exception("User with id ${quizEntity.userId} does not exist."))

            val createdQuiz = quizRepository.create(quizEntity.toQuiz())
                ?: return Result.failure(Exception("An error ocured during quiz creation"))

            Result.success(createdQuiz.toQuizEntity())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}