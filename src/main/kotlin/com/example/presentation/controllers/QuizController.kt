package com.example.presentation.controllers

import com.example.domain.entities.toQuizDto
import com.example.domain.usecases.image.SaveImageUseCase
import com.example.domain.usecases.quiz.CreateQuizUseCase
import com.example.domain.usecases.quiz.DeleteQuizUseCase
import com.example.domain.usecases.quiz.GetAllQuizzesUseCase
import com.example.presentation.dto.ApiResponse
import com.example.presentation.dto.Base64QuizDto
import com.example.presentation.dto.QuizDto
import com.example.presentation.dto.toQuizEntity

class QuizController(
    private val getAllQuizzesUseCase: GetAllQuizzesUseCase,
    private val saveImageUseCase: SaveImageUseCase,
    private val createQuizUseCase: CreateQuizUseCase,
    private val deleteQuizUseCase: DeleteQuizUseCase
) {
    suspend fun getAll(): ApiResponse<List<QuizDto>> {
        val result = getAllQuizzesUseCase()

        result.getOrNull()?.let { quizEntities ->
            return ApiResponse.Success(quizEntities.map { it.toQuizDto() })
        }

        return ApiResponse.Failure((result.exceptionOrNull() ?: Exception()).message ?: "Unknown exception.")
    }

    suspend fun create(base64QuizDto: Base64QuizDto): ApiResponse<QuizDto> {
        val quizImagePathResult = base64QuizDto.base64Image?.let { image ->
            saveImageUseCase(image)
        }

        val quizImagePath =
            if (quizImagePathResult == null) null
            else if (quizImagePathResult.isSuccess) quizImagePathResult.getOrNull()
                ?: return ApiResponse.Failure("Unknown error ocured when saving the image.")
            else return ApiResponse.Failure("Error saving the image.")

        val quizEntity = base64QuizDto.toQuizEntity(quizImagePath)
        val creationResult = createQuizUseCase(quizEntity)

        creationResult.getOrNull()?.let { quizEntity ->
            return ApiResponse.Success(quizEntity.toQuizDto())
        }

        val receivedException = creationResult.exceptionOrNull() ?: Exception()

        return ApiResponse.Failure(receivedException.message ?: "Unknown error ocured.")
    }

    suspend fun delete(quizId: Int): ApiResponse<Nothing?> {
        val result = deleteQuizUseCase(quizId)

        if (result.isSuccess) {
            return ApiResponse.Success(null)
        }

        val exception = result.exceptionOrNull() ?: Exception()

        return ApiResponse.Failure(exception.message ?: "Unknown error ocured")
    }

}