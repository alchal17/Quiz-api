package com.example.models.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object QuizQuestionOptions : IntIdTable("quiz_question_options") {
    val text = varchar("text", 100)
    val isCorrect = bool("is_correct")
    val quizQuestion = reference("quiz_question_id", QuizQuestions, onDelete = ReferenceOption.CASCADE)
}
