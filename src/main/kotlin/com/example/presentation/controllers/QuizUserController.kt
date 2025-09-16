package com.example.presentation.controllers

import com.example.domain.entities.toQuizUserDto
import com.example.domain.usecases.quizUser.*
import com.example.presentation.dto.ApiResponse
import com.example.presentation.dto.QuizUserDto
import com.example.presentation.dto.toEntity

class QuizUserController(
    private val createQuizUserUseCase: CreateQuizUserUseCase,
    private val getAllQuizUsersUseCase: GetAllQuizUsersUseCase,
    private val getQuizUserByIdUseCase: GetQuizUserByIdUseCase,
    private val deleteQuizUserUseCase: DeleteQuizUserUseCase,
    private val updateQuizUserUseCase: UpdateQuizUserUseCase
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

    suspend fun getById(id: Int): ApiResponse<QuizUserDto> {
        val result = getQuizUserByIdUseCase(id)

        result.getOrNull()?.let { userEntity ->
            return ApiResponse.Success(userEntity.toQuizUserDto())
        }

        return ApiResponse.Failure(result.exceptionOrNull()?.message ?: "Unknown error")
    }

    suspend fun delete(id: Int): ApiResponse<Nothing?> {
        val result = deleteQuizUserUseCase(id)
        return if (result.isSuccess) ApiResponse.Success(null) else ApiResponse.Failure(
            (result.exceptionOrNull() ?: Exception()).message ?: "Unknown exception."
        )
    }

    suspend fun update(quizUserDto: QuizUserDto): ApiResponse<QuizUserDto> {
        val result = updateQuizUserUseCase(quizUserDto.toEntity())
        result.getOrNull()?.let { quizUserEntity ->
            return ApiResponse.Success(quizUserEntity.toQuizUserDto())
        }
        return ApiResponse.Failure((result.exceptionOrNull() ?: Exception()).message ?: "Unknown exception.")
    }
}