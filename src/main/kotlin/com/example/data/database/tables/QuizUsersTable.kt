package com.example.data.database.tables

import org.jetbrains.exposed.dao.id.IntIdTable

object QuizUsersTable : IntIdTable("quiz_users") {
    val username = varchar("username", 20).uniqueIndex()
    val email = varchar("email", 50).uniqueIndex()
}

