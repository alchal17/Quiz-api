package com.example.domain.entities

import com.example.data.models.Quiz
import com.example.presentation.dto.Base64QuizDto
import com.example.presentation.dto.QuizDto

data class QuizEntity(
    val id: Int? = null,
    val name: String,
    val userId: Int,
    val description: String?,
    val imagePath: String?
)

fun QuizEntity.toQuizDto(): QuizDto = QuizDto(
    id = id,
    name = name,
    userId = userId,
    description = description,
    imagePath = imagePath
)

fun QuizEntity.toBase64QuizDto(base64Image: String?) = Base64QuizDto(
    id = id,
    name = name,
    userId = userId,
    description = description,
    base64Image = base64Image
)

fun QuizEntity.toQuiz(): Quiz = Quiz(
    id = id,
    name = name,
    userId = userId,
    description = description,
    imagePath = imagePath
)