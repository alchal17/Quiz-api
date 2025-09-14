package com.example.domain.entities

import com.example.data.models.QuizUser
import com.example.presentation.dto.QuizUserDto

data class QuizUserEntity(
    val id: Int? = null,
    val username: String,
    val email: String
)

fun QuizUserEntity.toQuizUserDto(): QuizUserDto = QuizUserDto(
    id = id,
    username = username,
    email = email
)

fun QuizUserEntity.toQuizUser(): QuizUser = QuizUser(
    id = id,
    username = username,
    email = email
)