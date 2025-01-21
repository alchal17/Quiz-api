package com.example.models.request_representation

import com.example.models.Model
import com.example.models.database_representation.Quiz
import com.example.models.database_representation.QuizQuestion
import com.example.models.database_representation.QuizUser
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DetailedQuiz(
    override val id: Int? = null,
    val name: String,
    @SerialName("image_path")
    val imagePath: String?,
    val description: String?,
    val questions: List<QuizQuestion>,
    val author: QuizUser,
) : Model {
    companion object {
        fun fromQuizAndAuthor(quiz: Quiz, author: QuizUser) = DetailedQuiz(
            id = quiz.id,
            name = quiz.name,
            imagePath = quiz.imagePath,
            description = quiz.description,
            questions = quiz.questions,
            author = author,
        )
    }
}