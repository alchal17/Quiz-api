package com.example.domain.usecases.quiz

import com.example.data.repositories.filesHandlers.FileHandlerRepository
import com.example.data.repositories.quizRpository.QuizRepository

class DeleteQuizUseCase(
    private val quizRepository: QuizRepository,
    private val fileHandlerRepository: FileHandlerRepository
) {
    suspend operator fun invoke(quizId: Int): Result<Nothing?> {
        return try {
            val quiz = quizRepository.getById(quizId)
                ?: return Result.failure(Exception("Quiz with id $quizId does not exist"))
            quiz.imagePath?.let { imagePath ->
                fileHandlerRepository.delete(imagePath)
            }
            val result = quizRepository.delete(quizId)
            return if (result) Result.success(null) else Result.failure(Exception("Unknown error ocured while deleting quiz $quizId."))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}