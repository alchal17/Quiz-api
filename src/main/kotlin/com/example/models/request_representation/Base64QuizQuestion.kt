package com.example.models.request_representation

import com.example.models.Model
import com.example.models.database_representation.QuizQuestion
import com.example.models.database_representation.QuizQuestionOption
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Base64QuizQuestion(
    override val id: Int? = null,
    val text: String,
    val description: String?,
    @SerialName("base64_image")
    val base64Image: String?,
    @SerialName("multiple_choices")
    val multipleChoices: Boolean,
    val options: List<QuizQuestionOption>
) : Model {
    companion object {
        fun toQuizQuestion(base64QuizQuestion: Base64QuizQuestion, imagePath: String?): QuizQuestion {
            return QuizQuestion(
                id = base64QuizQuestion.id,
                text = base64QuizQuestion.text,
                description = base64QuizQuestion.description,
                imagePath = imagePath,
                multipleChoices = base64QuizQuestion.multipleChoices,
                options = base64QuizQuestion.options
            )
        }
    }
}
