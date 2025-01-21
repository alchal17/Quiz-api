package com.example.models.database_representation

import com.example.models.Model
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object Quizzes : IntIdTable("quizzes") {
    val name = varchar("name", 255)
    val user = reference("user_id", QuizUsers, onDelete = ReferenceOption.CASCADE)
    val description = text("description").nullable()
    val imagePath = varchar("image_path", 100).nullable()
}

@Serializable
data class Quiz(
    override val id: Int? = null,
    val name: String,
    @SerialName("image_path")
    val imagePath: String?,
    val description: String?,
    val questions: List<QuizQuestion>
) : Model
