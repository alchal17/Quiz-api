package com.example.domain.usecases.quizUser

import com.example.data.repositories.quizUserRepository.QuizUserRepository

class DeleteQuizUserUseCase(private val quizUserRepository: QuizUserRepository) {
    suspend operator fun invoke(quizUserId: Int): Result<Nothing?> {
        return try {
            quizUserRepository.getById(quizUserId)
                ?: return Result.failure(Exception("User with id $quizUserId does not exist."))
            val result = quizUserRepository.delete(quizUserId)
            return if (result) Result.success(null) else Result.failure<Nothing>(
                Exception(
                    "An error ocured while deleting the user with id $quizUserId."
                )
            )
        } catch (e: Exception) {
            Result.failure<Nothing>(Exception(e))
        }
    }
}