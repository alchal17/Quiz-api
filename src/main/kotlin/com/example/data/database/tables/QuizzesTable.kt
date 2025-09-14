package com.example.data.database.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object QuizzesTable : IntIdTable("quizzes") {
    val name = varchar("name", 255)
    val user = reference("user_id", QuizUsersTable, onDelete = ReferenceOption.CASCADE)
    val description = text("description").nullable()
    val imagePath = varchar("image_path", 100).nullable()
}
