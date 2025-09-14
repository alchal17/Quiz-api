package com.example.data.database.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object QuizQuestionOptionsTable : IntIdTable("quiz_question_options") {
    val text = varchar("text", 100)
    val isCorrect = bool("is_correct")
    val quizQuestion = reference("quiz_question_id", QuizQuestionsTable, onDelete = ReferenceOption.CASCADE)
}
