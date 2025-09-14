package com.example.presentation.dto

import com.example.data.models.Model
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

fun Base64QuizDto.toQuiz(filePath: String?): QuizDto {
    return QuizDto(
        id = id,
        name = name,
        userId = userId,
        description = description,
        imagePath = filePath
    )
}