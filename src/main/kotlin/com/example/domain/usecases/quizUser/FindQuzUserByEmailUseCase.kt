package com.example.domain.usecases.quizUser

import com.example.data.models.toQuizUserEntity
import com.example.data.repositories.quizUserRepository.QuizUserRepository
import com.example.domain.entities.QuizUserEntity

class FindQuzUserByEmailUseCase(private val quizUserRepository: QuizUserRepository) {
    suspend operator fun invoke(email: String): Result<QuizUserEntity> {
        val searchingResult =
            quizUserRepository.findByEmail(email) ?: return Result.failure(Exception("No user with email $email."))

        return Result.success(searchingResult.toQuizUserEntity())
    }
}