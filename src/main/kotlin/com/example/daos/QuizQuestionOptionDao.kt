package com.example.daos

import com.example.models.database_representation.QuizQuestionOption
import com.example.models.database_representation.QuizQuestionOptions
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class QuizQuestionOptionDao(private val finder: Finder) :
    Dao<QuizQuestionOption>(QuizQuestionOptions) {
    override fun toEntity(row: ResultRow): QuizQuestionOption {
        val quizQuestionId = row[QuizQuestionOptions.quizQuestion].value
        return QuizQuestionOption(
            id = row[QuizQuestionOptions.id].value,
            text = row[QuizQuestionOptions.text],
            isCorrect = row[QuizQuestionOptions.isCorrect],
            quizQuestion = finder.findQuizQuestionById(quizQuestionId) ?: throw Exception("Unknown quiz question id"),
        )
    }

    override fun add(entity: QuizQuestionOption): Int {
        return transaction {
            QuizQuestionOptions.insertAndGetId { row ->
                row[text] = entity.text
                row[isCorrect] = entity.isCorrect
                row[quizQuestion] =
                    entity.quizQuestion.id ?: throw IllegalArgumentException("Unknown quiz question id.")
            }.value
        }
    }

    override fun update(id: Int, entity: QuizQuestionOption) {
        QuizQuestionOptions.update({ QuizQuestionOptions.id eq id }) { row ->
            row[text] = entity.text
            row[isCorrect] = entity.isCorrect
            row[quizQuestion] =
                entity.quizQuestion.id ?: throw IllegalArgumentException("Unknown quiz question id.")
        }
    }

    fun findByQuizQuestionId(quizQuestionId: Int): List<QuizQuestionOption> {
        return transaction {
            QuizQuestionOptions.selectAll().where { QuizQuestionOptions.quizQuestion eq quizQuestionId }.map { row ->
                toEntity(row)
            }
        }
    }
}