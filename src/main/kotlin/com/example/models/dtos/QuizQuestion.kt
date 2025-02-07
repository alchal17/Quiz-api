package com.example.models.dtos

import com.example.models.Model
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuizQuestion(
    override val id: Int? = null,
    @SerialName("quiz_id")
    val quizId: Int,
    val text: String,
    @SerialName("image_path")
    val imagePath: String?,
    @SerialName("multiple_choices")
    val multipleChoices: Boolean,
    @SerialName("seconds_to_answer")
    val secondsToAnswer: Int
) : Model

fun QuizQuestion.toBase64QuizQuestion(base64Image: String?): Base64QuizQuestion{
    return Base64QuizQuestion(
        id = id,
        quizId = quizId,
        text = text,
        base64Image = base64Image,
        multipleChoices = multipleChoices,
        secondsToAnswer = secondsToAnswer
    )
}