package com.example.domain.usecases.quizUser

import com.example.data.models.toQuizUserEntity
import com.example.data.repositories.quizUserRepository.QuizUserRepository
import com.example.domain.entities.QuizUserEntity

class FindQuizUserByUsernameUseCase(private val quizUserRepository: QuizUserRepository) {
    suspend operator fun invoke(username: String): Result<QuizUserEntity> {
        val quizUser = quizUserRepository.findByUsername(username)
            ?: return Result.failure(Exception("No user with username $username has been found."))

        return Result.success(quizUser.toQuizUserEntity())
    }
}