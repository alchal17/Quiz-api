package com.example.presentation.controllers

import com.example.domain.entities.toQuizDto
import com.example.domain.usecases.quiz.GetAllQuizzesUseCase
import com.example.presentation.dto.ApiResponse
import com.example.presentation.dto.QuizDto

class QuizController(
    private val getAllQuizzesUseCase: GetAllQuizzesUseCase
) {
    suspend fun getAll(): ApiResponse<List<QuizDto>> {
        val result = getAllQuizzesUseCase()

        result.getOrNull()?.let { quizEntities ->
            return ApiResponse.Success(quizEntities.map { it.toQuizDto() })
        }

        return ApiResponse.Failure((result.exceptionOrNull() ?: Exception()).message ?: "Unknown exception.")
    }
}