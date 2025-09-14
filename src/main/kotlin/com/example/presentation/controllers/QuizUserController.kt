package com.example.presentation.controllers

import com.example.domain.entities.toQuizUserDto
import com.example.domain.usecases.quizUser.CreateQuizUserUseCase
import com.example.domain.usecases.quizUser.GetAllQuizUsersUseCase
import com.example.presentation.dto.ApiResponse
import com.example.presentation.dto.QuizUserDto
import com.example.presentation.dto.toEntity

class QuizUserController(
    private val createQuizUserUseCase: CreateQuizUserUseCase,
    private val getAllQuizUsersUseCase: GetAllQuizUsersUseCase
) {
    suspend fun create(quizUserDto: QuizUserDto): ApiResponse<QuizUserDto> {
        val quizUserEntity = quizUserDto.toEntity()

        val result = createQuizUserUseCase(quizUserEntity)

        result.getOrNull()?.let { quizUserEntity ->
            return ApiResponse.Success(quizUserEntity.toQuizUserDto())
        }

        return ApiResponse.Failure(result.exceptionOrNull()?.message ?: "Unknown error")
    }

    suspend fun getAll(): ApiResponse<List<QuizUserDto>> {
        val result = getAllQuizUsersUseCase()

        result.getOrNull()?.let { userEntities ->
            return ApiResponse.Success(userEntities.map { it.toQuizUserDto() })
        }

        return ApiResponse.Failure(result.exceptionOrNull()?.message ?: "Unknown error")

    }
}