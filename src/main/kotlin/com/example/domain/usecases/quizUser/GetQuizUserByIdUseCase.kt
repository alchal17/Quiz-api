package com.example.domain.usecases.quizUser

import com.example.data.models.toQuizUserEntity
import com.example.data.repositories.quizUserRepository.QuizUserRepository
import com.example.domain.entities.QuizUserEntity

class GetQuizUserByIdUseCase(private val quizUserRepository: QuizUserRepository) {
    suspend operator fun invoke(id: Int): Result<QuizUserEntity> {
        val quizUser = quizUserRepository.getById(id) ?: return Result.failure(Exception("User not found"))
        return Result.success(quizUser.toQuizUserEntity())
    }
}