package com.example.dto

import com.example.models.Model
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Quiz(
    override val id: Int? = null,
    val name: String,
    @SerialName("user_id")
    val userId: Int,
    val description: String?,
    @SerialName("image_path")
    val imagePath: String?,
) : Model

fun Quiz.toBase64Quiz(base64Image: String?): Base64Quiz {
    return Base64Quiz(
        id = id,
        name = name,
        userId = userId,
        description = description,
        base64Image = base64Image,
    )
}