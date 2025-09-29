package com.example.presentation.dto

import com.example.data.models.Model
import com.example.domain.entities.QuizEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class Base64QuizDto(
    override val id: Int? = null,
    val name: String,
    @SerialName("user_id")
    val userId: Int,
    val description: String?,
    @SerialName("base64_image")
    val base64Image: String?,
) : Model

fun Base64QuizDto.toQuizEntity(imagePath: String?): QuizEntity = QuizEntity(
    id = id,
    name = name,
    userId = userId,
    description = description,
    imagePath = imagePath
)