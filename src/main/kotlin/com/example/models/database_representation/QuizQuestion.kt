package com.example.models.database_representation

import com.example.models.Model
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object QuizQuestions : IntIdTable("quiz_questions") {
    val quiz = reference("quiz_id", Quizzes, onDelete = ReferenceOption.CASCADE)
    val text = varchar("text", 255)
    val imagePath = varchar("image_path", 100).nullable()
    val multipleChoices = bool("multiple_choices")
    val secondsToAnswer = integer("seconds_answer")
}

@Serializable
data class QuizQuestion(
    override val id: Int? = null,
    val text: String,
    @SerialName("image_path")
    val imagePath: String?,
    @SerialName("multiple_choices")
    val multipleChoices: Boolean,
    val options: List<QuizQuestionOption>,
    @SerialName("seconds_to_answer")
    val secondsToAnswer: Int
) : Model