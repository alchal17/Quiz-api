package com.example.presentation.dto

import com.example.data.repositories.filesHandlers.FileHandlerRepository
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
    @SerialName("oder_number")
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
        orderNumber = this@toBase64QuizQuestion.orderNumber
    )
}

fun List<QuizQuestionDto>.toBase64QuizQuestions(fileHandlerRepository: FileHandlerRepository): List<Base64QuizQuestionDto> {
    return this.map { quizQuestion ->
        val base64Image = quizQuestion.imagePath?.let { fileHandlerRepository.encodeImageToBase64(it) }
        quizQuestion.toBase64QuizQuestion(base64Image)
    }
}