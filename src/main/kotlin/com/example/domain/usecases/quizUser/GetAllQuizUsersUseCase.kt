package com.example.domain.usecases.quizUser

import com.example.data.models.toQuizUserEntity
import com.example.data.repositories.quizUserRepository.QuizUserRepository
import com.example.domain.entities.QuizUserEntity

class GetAllQuizUsersUseCase(private val quizUserRepository: QuizUserRepository) {
    suspend operator fun invoke(): Result<List<QuizUserEntity>> {
        return try {
            val quizUsers = quizUserRepository.getAll().map { it.toQuizUserEntity() }
            Result.success(quizUsers)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}