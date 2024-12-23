package com.example.models.database_representation

import com.example.models.Model
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable

object QuizUsers : IntIdTable("quiz_users") {
    val username = varchar("username", 20).uniqueIndex()
    val email = varchar("email", 50).uniqueIndex()
}

@Serializable
data class QuizUser(override val id: Int? = null, val username: String, val email: String) : Model
