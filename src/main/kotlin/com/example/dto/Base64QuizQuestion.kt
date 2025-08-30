package com.example.dto

import com.example.models.Model
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Base64QuizQuestion(
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
    @SerialName("oder_number")
    val orderNumber: Int
) : Model

fun Base64QuizQuestion.toQuizQuestion(imagePath: String?): QuizQuestion {
    return QuizQuestion(
        id = id,
        quizId = quizId,
        text = text,
        imagePath = imagePath,
        multipleChoices = multipleChoices,
        secondsToAnswer = secondsToAnswer,
        orderNumber = orderNumber
    )
}