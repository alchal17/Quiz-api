package com.example.models.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object Quizzes : IntIdTable("quizzes") {
    val name = varchar("name", 255)
    val user = reference("user_id", QuizUsers, onDelete = ReferenceOption.CASCADE)
    val description = text("description").nullable()
    val imagePath = varchar("image_path", 100).nullable()
}
