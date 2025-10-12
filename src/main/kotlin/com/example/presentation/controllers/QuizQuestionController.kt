package com.example.presentation.controllers

import com.example.domain.entities.toQuizQuestionDto
import com.example.domain.usecases.quizQuestions.CreateQuizQuestionUseCase
import com.example.domain.usecases.quizQuestions.GetAllQuizQuestionsUseCase
import com.example.domain.usecases.quizQuestions.GetQuizQuestionByIdUseCase
import com.example.presentation.dto.ApiResponse
import com.example.presentation.dto.Base64QuizQuestionDto
import com.example.presentation.dto.QuizQuestionDto
import com.example.presentation.dto.toQuizQuestionEntity

class QuizQuestionController(
    private val getAllQuizQuestionsUseCase: GetAllQuizQuestionsUseCase,
    private val getQuizQuestionByIdUseCase: GetQuizQuestionByIdUseCase,
    private val createQuizQuestionUseCase: CreateQuizQuestionUseCase
) {
    suspend fun getAll(): ApiResponse<List<QuizQuestionDto>> {
        val result = getAllQuizQuestionsUseCase()

        result.getOrNull()?.let { quizQuestionEntities ->
            return ApiResponse.Success(quizQuestionEntities.map { it.toQuizQuestionDto() })
        }

        val exception = result.exceptionOrNull() ?: Exception()

        return ApiResponse.Failure(exception.message ?: "Unknown error has ocured.")
    }

    suspend fun getById(quizQuestionId: Int): ApiResponse<QuizQuestionDto> {
        val result = getQuizQuestionByIdUseCase(quizQuestionId)

        result.getOrNull()?.let { quizQuestionEntity ->
            return ApiResponse.Success(quizQuestionEntity.toQuizQuestionDto())
        }

        val exception = result.exceptionOrNull() ?: Exception()

        return ApiResponse.Failure(exception.message ?: "Unknown error has ocured.")
    }

    suspend fun create(base64QuizQuestionDto: Base64QuizQuestionDto): ApiResponse<QuizQuestionDto> {
        val quizQuestionEntity = base64QuizQuestionDto.toQuizQuestionEntity()

        val result = createQuizQuestionUseCase(quizQuestionEntity, base64QuizQuestionDto.base64Image)

        result.getOrNull()?.let { quizQuestionEntity ->
            return ApiResponse.Success(quizQuestionEntity.toQuizQuestionDto())
        }

        val exception = result.exceptionOrNull() ?: Exception()

        return ApiResponse.Failure(exception.message ?: "Uknown error ocured")

    }
}