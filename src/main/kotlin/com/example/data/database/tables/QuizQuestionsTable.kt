package com.example.data.database.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object QuizQuestionsTable : IntIdTable("quiz_questions") {
    val quiz = reference("quiz_id", QuizzesTable, onDelete = ReferenceOption.CASCADE)
    val text = varchar("text", 255)
    val imagePath = varchar("image_path", 100).nullable()
    val multipleChoices = bool("multiple_choices")
    val secondsToAnswer = integer("seconds_answer")
    val orderNumber = integer("order_number")
}

