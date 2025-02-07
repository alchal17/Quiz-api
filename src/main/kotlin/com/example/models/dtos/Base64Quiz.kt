package com.example.models.dtos

import com.example.models.Model
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class Base64Quiz(
    override val id: Int? = null,
    val name: String,
    @SerialName("user_id")
    val userId: Int,
    val description: String?,
    @SerialName("base64_image")
    val base64Image: String?,
) : Model

fun Base64Quiz.toQuiz(filePath: String?): Quiz {
    return Quiz(
        id = id,
        name = name,
        userId = userId,
        description = description,
        imagePath = filePath
    )
}