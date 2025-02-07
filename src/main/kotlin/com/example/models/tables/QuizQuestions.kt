package com.example.models.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object QuizQuestions : IntIdTable("quiz_questions") {
    val quiz = reference("quiz_id", Quizzes, onDelete = ReferenceOption.CASCADE)
    val text = varchar("text", 255)
    val imagePath = varchar("image_path", 100).nullable()
    val multipleChoices = bool("multiple_choices")
    val secondsToAnswer = integer("seconds_answer")
}

