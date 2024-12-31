package com.example.models.database_representation

import com.example.models.Model
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object QuizQuestionOptions : IntIdTable("quiz_question_options") {
    val text = varchar("text", 100)
    val isCorrect = bool("is_correct")
    val quizQuestion = reference("quiz_question_id", QuizQuestions, onDelete = ReferenceOption.CASCADE)
}

@Serializable
data class QuizQuestionOption(
    override val id: Int? = null,
    val text: String,
    val isCorrect: Boolean,
) : Model