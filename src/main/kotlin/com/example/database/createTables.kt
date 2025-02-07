package com.example.database

import com.example.models.tables.QuizQuestionOptions
import com.example.models.tables.QuizQuestions
import com.example.models.tables.QuizUsers
import com.example.models.tables.Quizzes
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.transaction

fun createTables() {
    val tables = listOf<Table>(QuizUsers, Quizzes, QuizQuestions, QuizQuestionOptions)
    tables.forEach { table -> transaction { SchemaUtils.create(table) } }
}