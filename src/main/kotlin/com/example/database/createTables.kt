package com.example.database

import com.example.models.database_representation.QuizQuestionOptions
import com.example.models.database_representation.QuizQuestions
import com.example.models.database_representation.QuizUsers
import com.example.models.database_representation.Quizzes
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.transaction

fun createTables() {
    val tables = listOf<Table>(QuizUsers, Quizzes, QuizQuestions, QuizQuestionOptions)
    tables.forEach { table -> transaction { SchemaUtils.create(table) } }
}