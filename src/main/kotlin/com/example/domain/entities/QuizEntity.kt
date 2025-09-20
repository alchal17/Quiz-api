package com.example.domain.entities

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