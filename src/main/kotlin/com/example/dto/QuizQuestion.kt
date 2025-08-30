package com.example.dto

import com.example.files_handlers.FileHandler
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
    val secondsToAnswer: Int,
    @SerialName("oder_number")
    val orderNumber: Int
) : Model

fun QuizQuestion.toBase64QuizQuestion(base64Image: String?): Base64QuizQuestion {
    return Base64QuizQuestion(
        id = id,
        quizId = quizId,
        text = text,
        base64Image = base64Image,
        multipleChoices = multipleChoices,
        secondsToAnswer = secondsToAnswer,
        orderNumber = this@toBase64QuizQuestion.orderNumber
    )
}

fun List<QuizQuestion>.toBase64QuizQuestions(fileHandler: FileHandler): List<Base64QuizQuestion> {
    return this.map { quizQuestion ->
        val base64Image = quizQuestion.imagePath?.let { fileHandler.encodeImageToBase64(it) }
        quizQuestion.toBase64QuizQuestion(base64Image)
    }
}