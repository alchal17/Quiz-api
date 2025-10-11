package com.example.presentation.dto

import com.example.data.models.Model
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuizQuestionDto(
    override val id: Int? = null,
    @SerialName("quiz_id")
    val quizId: Int,
    val text: String,
    @SerialName("image_path")
    val imagePath: String?,
    @SerialName("multiple_choices")
    val multipleChoices: Boolean,
    @SerialName("seconds_to_answer")
    val secondsToAnswer: Int,
    @SerialName("order_number")
    val orderNumber: Int
) : Model

fun QuizQuestionDto.toBase64QuizQuestion(base64Image: String?): Base64QuizQuestionDto {
    return Base64QuizQuestionDto(
        id = id,
        quizId = quizId,
        text = text,
        base64Image = base64Image,
        multipleChoices = multipleChoices,
        secondsToAnswer = secondsToAnswer,
        orderNumber = orderNumber
    )
}