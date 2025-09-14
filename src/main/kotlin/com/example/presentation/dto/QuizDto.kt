package com.example.presentation.dto

import com.example.data.models.Model
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuizDto(
    override val id: Int? = null,
    val name: String,
    @SerialName("user_id")
    val userId: Int,
    val description: String?,
    @SerialName("image_path")
    val imagePath: String?,
) : Model

fun QuizDto.toBase64Quiz(base64Image: String?): Base64QuizDto {
    return Base64QuizDto(
        id = id,
        name = name,
        userId = userId,
        description = description,
        base64Image = base64Image,
    )
}