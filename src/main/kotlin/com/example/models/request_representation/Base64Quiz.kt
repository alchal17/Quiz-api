package com.example.models.request_representation

import com.example.models.Model
import com.example.models.database_representation.Quiz
import com.example.models.database_representation.QuizQuestion
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Base64Quiz(
    override val id: Int? = null,
    val name: String,
    @SerialName("user_id")
    val userId: Int,
    @SerialName("base64_image")
    val base64Image: String?,
    val description: String?,
    val questions: List<Base64QuizQuestion>,
) : Model {
    companion object {
        fun toQuiz(base64Quiz: Base64Quiz, imagePath: String?, quizQuestions: List<QuizQuestion>): Quiz {
            return Quiz(
                id = base64Quiz.id,
                name = base64Quiz.name,
                imagePath = imagePath,
                description = base64Quiz.description,
                questions = quizQuestions,
            )
        }

        fun fromQuiz(
            quiz: Quiz,
            userId: Int,
            base64Image: String?,
            base64Questions: List<Base64QuizQuestion>
        ): Base64Quiz {
            return Base64Quiz(
                id = quiz.id,
                name = quiz.name,
                userId = userId,
                base64Image = base64Image,
                description = quiz.description,
                questions = base64Questions,
            )
        }
    }
}
