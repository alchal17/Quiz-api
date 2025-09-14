package com.example.domain.usecases.quizUser

import com.example.data.models.toQuizUserEntity
import com.example.data.repositories.quizUserRepository.QuizUserRepository
import com.example.domain.entities.QuizUserEntity
import com.example.domain.entities.toQuizUser

class CreateQuizUserUseCase(private val quizUserRepository: QuizUserRepository) {
    suspend operator fun invoke(quizUserEntity: QuizUserEntity): Result<QuizUserEntity> {
        return try {
            val existingUserByEmail = quizUserRepository.findByEmail(quizUserEntity.email)
            if (existingUserByEmail != null) {
                return Result.failure(Exception("User with such email already exists."))
            }
            val quizUser = quizUserEntity.toQuizUser()
            val createdQuizUser =
                quizUserRepository.create(quizUser) ?: return Result.failure(Exception("Failed to create user."))
            Result.success(createdQuizUser.toQuizUserEntity())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}