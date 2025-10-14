package com.example.domain.usecases.quizQuestions

import com.example.data.models.toQuizQuestionEntity
import com.example.data.repositories.filesHandlers.FileHandlerRepository
import com.example.data.repositories.quizQuestionRepository.QuizQuestionRepository
import com.example.domain.entities.QuizQuestionEntity
import com.example.domain.entities.toQuizQuestion
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class UpdateQuizQuestionUseCase(
    private val fileHandlerRepository: FileHandlerRepository,
    private val quizQuestionRepository: QuizQuestionRepository
) {
    suspend operator fun invoke(
        quizQuestionEntity: QuizQuestionEntity,
        base64Image: String?
    ): Result<QuizQuestionEntity> =
        coroutineScope {
            val quizQuestionId =
                quizQuestionEntity.id ?: return@coroutineScope Result.failure(Exception("No id has been provided"))

            val pastQuizQuestion =
                quizQuestionRepository.getById(quizQuestionId) ?: return@coroutineScope Result.failure(
                    Exception("No quiz question with id $quizQuestionId.")
                )

            val insertImageTask = base64Image?.let {
                async(Dispatchers.IO) {
                    fileHandlerRepository.saveImage(it, "/quiz_question_images")
                }
            }

            pastQuizQuestion.imagePath?.let {
                launch(Dispatchers.IO) {
                    fileHandlerRepository.delete(it)
                }
            }

            val savedImagePath = insertImageTask?.await()

            val quizQuestion = quizQuestionEntity.toQuizQuestion()

            val result = quizQuestionRepository.update(quizQuestion.copy(imagePath = savedImagePath))

            return@coroutineScope if (result == null) {
                Result.failure(Exception("An error ocured while updating quiz question $quizQuestionId."))
            } else {
                Result.success(result.toQuizQuestionEntity())
            }
        }
}