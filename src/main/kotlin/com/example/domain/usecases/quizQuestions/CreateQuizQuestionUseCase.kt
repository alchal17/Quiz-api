package com.example.domain.usecases.quizQuestions

import com.example.data.models.toQuizQuestionEntity
import com.example.data.repositories.filesHandlers.FileHandlerRepository
import com.example.data.repositories.quizQuestionRepository.QuizQuestionRepository
import com.example.data.repositories.quizRpository.QuizRepository
import com.example.domain.entities.QuizQuestionEntity
import com.example.domain.entities.toQuizQuestion
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CreateQuizQuestionUseCase(
    private val quizRepository: QuizRepository,
    private val quizQuestionRepository: QuizQuestionRepository,
    private val fileHandlerRepository: FileHandlerRepository
) {
    suspend operator fun invoke(
        quizQuestionEntity: QuizQuestionEntity,
        base64Image: String?
    ): Result<QuizQuestionEntity> {
        return try {
            val quizId = quizQuestionEntity.quizId

            quizRepository.getById(quizId)
                ?: return Result.failure(Exception("Quiz with id $quizId does not exist"))

            val order = quizQuestionEntity.orderNumber

            quizQuestionRepository.getByQuizIdAndOrder(quizId, order)
                ?.let {
                    val exception = Exception("A question at position $order for quiz $quizId already exists")
                    return Result.failure(exception)
                }

            val imagePath = base64Image?.let { image ->
                withContext(Dispatchers.IO) {
                    fileHandlerRepository.saveImage(image, "/quiz_question_images")
                }
            }

            val quizQuestionWithSavedImage = quizQuestionEntity.copy(imagePath = imagePath)

            val savedQuizQuestion = quizQuestionRepository.create(quizQuestionWithSavedImage.toQuizQuestion())
                ?: return Result.failure(Exception("An error ocured while creating the quiz question."))

            Result.success(savedQuizQuestion.toQuizQuestionEntity())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}