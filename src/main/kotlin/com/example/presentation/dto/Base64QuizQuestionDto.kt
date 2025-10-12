package com.example.presentation.dto

import com.example.data.models.Model
import com.example.domain.entities.QuizQuestionEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Base64QuizQuestionDto(
    override val id: Int? = null,
    @SerialName("quiz_id")
    val quizId: Int,
    val text: String,
    @SerialName("base64_image")
    val base64Image: String?,
    @SerialName("multiple_choices")
    val multipleChoices: Boolean,
    @SerialName("seconds_to_answer")
    val secondsToAnswer: Int,
    @SerialName("order_number")
    val orderNumber: Int
) : Model

fun Base64QuizQuestionDto.toQuizQuestionEntity(imagePath: String? = null): QuizQuestionEntity {
    return QuizQuestionEntity(
        id = id,
        quizId = quizId,
        text = text,
        imagePath = imagePath,
        multipleChoices = multipleChoices,
        secondsToAnswer = secondsToAnswer,
        orderNumber = orderNumber
    )
}