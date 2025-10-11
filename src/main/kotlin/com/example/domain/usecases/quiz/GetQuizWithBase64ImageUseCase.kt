package com.example.domain.usecases.quiz

import com.example.data.models.toQuizEntity
import com.example.data.repositories.filesHandlers.FileHandlerRepository
import com.example.data.repositories.quizRpository.QuizRepository
import com.example.domain.entities.QuizEntity

class GetQuizWithBase64ImageUseCase(
    private val quizRepository: QuizRepository,
    private val fileHandlerRepository: FileHandlerRepository
) {
    suspend operator fun invoke(quizId: Int): Result<Pair<QuizEntity, String?>> {
        val quiz =
            quizRepository.getById(quizId) ?: return Result.failure(Exception("Quiz with id $quizId does not exist."))

        val base64Image = quiz.imagePath?.let { imagePath ->
            fileHandlerRepository.encodeImageToBase64(imagePath)
        }

        return Result.success(Pair(quiz.toQuizEntity(), base64Image))
    }
}