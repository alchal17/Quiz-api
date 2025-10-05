package com.example.presentation.controllers

import com.example.domain.entities.toQuizDto
import com.example.domain.usecases.quiz.*
import com.example.presentation.dto.ApiResponse
import com.example.presentation.dto.Base64QuizDto
import com.example.presentation.dto.QuizDto
import com.example.presentation.dto.toQuizEntity

class QuizController(
    private val getAllQuizzesUseCase: GetAllQuizzesUseCase,
    private val createQuizUseCase: CreateQuizUseCase,
    private val deleteQuizUseCase: DeleteQuizUseCase,
    private val getQuizByIdUseCase: GetQuizByIdUseCase,
    private val updateQuizUseCase: UpdateQuizUseCase
) {
    suspend fun getAll(): ApiResponse<List<QuizDto>> {
        val result = getAllQuizzesUseCase()

        result.getOrNull()?.let { quizEntities ->
            return ApiResponse.Success(quizEntities.map { it.toQuizDto() })
        }

        return ApiResponse.Failure((result.exceptionOrNull() ?: Exception()).message ?: "Unknown exception.")
    }

    suspend fun getById(quizId: Int): ApiResponse<QuizDto> {
        val result = getQuizByIdUseCase.invoke(quizId)

        result.getOrNull()?.let { quizEntity ->
            return ApiResponse.Success(quizEntity.toQuizDto())
        }

        val exception = result.exceptionOrNull() ?: Exception()

        return ApiResponse.Failure(exception.message ?: "Unknown error ocured.")
    }

    suspend fun create(base64QuizDto: Base64QuizDto): ApiResponse<QuizDto> {

        val quizEntity = base64QuizDto.toQuizEntity()
        val creationResult = createQuizUseCase(quizEntity, base64QuizDto.base64Image)

        creationResult.getOrNull()?.let { quizEntity ->
            return ApiResponse.Success(quizEntity.toQuizDto())
        }

        val receivedException = creationResult.exceptionOrNull() ?: Exception()

        return ApiResponse.Failure(receivedException.message ?: "Unknown error ocured.")
    }

    suspend fun update(quizDto: Base64QuizDto): ApiResponse<QuizDto>  {
        val quizEntity = quizDto.toQuizEntity()
        val result = updateQuizUseCase(quizEntity, quizDto.base64Image)

        result.getOrNull()?.let { quizEntity ->
            return ApiResponse.Success(quizEntity.toQuizDto())
        }

        val exception = result.exceptionOrNull() ?: Exception()

        return ApiResponse.Failure(exception.message ?: "Unknown error ocured")
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