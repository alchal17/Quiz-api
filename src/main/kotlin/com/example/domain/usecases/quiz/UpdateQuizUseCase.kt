package com.example.domain.usecases.quiz

import com.example.data.models.toQuizEntity
import com.example.data.repositories.filesHandlers.FileHandlerRepository
import com.example.data.repositories.quizRpository.QuizRepository
import com.example.data.repositories.quizUserRepository.QuizUserRepository
import com.example.domain.entities.QuizEntity
import com.example.domain.entities.toQuiz
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class UpdateQuizUseCase(
    private val quizRepository: QuizRepository,
    private val fileHandlerRepository: FileHandlerRepository,
    private val quizUserRepository: QuizUserRepository
) {
    suspend operator fun invoke(quizEntity: QuizEntity, base64Image: String?): Result<QuizEntity> = coroutineScope {
        val quizId = quizEntity.id ?: return@coroutineScope Result.failure(Exception("No id has been provided."))

        val pastQuiz =
            quizRepository.getById(quizId)
                ?: return@coroutineScope Result.failure(Exception("Quiz with id $quizId does not exist."))

        quizUserRepository.getById(quizEntity.userId)
            ?: return@coroutineScope Result.failure(Exception("User with id ${quizEntity.userId} does not exist."))

        val pastQuizImagePath = pastQuiz.imagePath

        // This task will be null if the value of base64Image is null
        val saveImageNullableTask = base64Image?.let { image ->
            async(Dispatchers.IO) { fileHandlerRepository.saveImage(image, "/quiz_images") }
        }

        // This task will be null if the value of pastQuizImagePath is null
        val deleteImageNullableTask = pastQuizImagePath?.let { image ->
            async(Dispatchers.IO) { fileHandlerRepository.delete(image) }
        }


        val resultImagePath = saveImageNullableTask?.await()
        deleteImageNullableTask?.await()


        val result = quizRepository.update(quizEntity.copy(imagePath = resultImagePath).toQuiz())

        return@coroutineScope if (result == null) {
            Result.failure(Exception("An error ocured while updating quiz $quizId"))
        } else {
            Result.success(result.toQuizEntity())
        }
    }
}