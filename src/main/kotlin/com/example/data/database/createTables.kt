package com.example.data.database

import com.example.data.database.tables.QuizQuestionOptionsTable
import com.example.data.database.tables.QuizQuestionsTable
import com.example.data.database.tables.QuizUsersTable
import com.example.data.database.tables.QuizzesTable
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.transaction

fun createTables() {
    val tables = listOf<Table>(QuizUsersTable, QuizzesTable, QuizQuestionsTable, QuizQuestionOptionsTable)
    tables.forEach { table -> transaction { SchemaUtils.create(table) } }
}