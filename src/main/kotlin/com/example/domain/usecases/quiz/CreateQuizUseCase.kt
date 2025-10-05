package com.example.domain.usecases.quiz

import com.example.data.models.toQuizEntity
import com.example.data.repositories.filesHandlers.FileHandlerRepository
import com.example.data.repositories.quizRpository.QuizRepository
import com.example.data.repositories.quizUserRepository.QuizUserRepository
import com.example.domain.entities.QuizEntity
import com.example.domain.entities.toQuiz

class CreateQuizUseCase(
    private val quizRepository: QuizRepository,
    private val quizUserRepository: QuizUserRepository,
    private val fileHandlerRepository: FileHandlerRepository
) {
    suspend operator fun invoke(quizEntity: QuizEntity, base64Image: String?): Result<QuizEntity> {
        return try {

            quizUserRepository.getById(quizEntity.userId)
                ?: return Result.failure(Exception("User with id ${quizEntity.userId} does not exist."))

            val savedFilePath = base64Image?.let {
                fileHandlerRepository.saveImage(it, "/quiz_images")
            }

            val quizWithSavedFile = quizEntity.copy(imagePath = savedFilePath)

            val createdQuiz = quizRepository.create(quizWithSavedFile.toQuiz())
                ?: return Result.failure(Exception("An error ocured during quiz creation"))

            Result.success(createdQuiz.toQuizEntity())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}