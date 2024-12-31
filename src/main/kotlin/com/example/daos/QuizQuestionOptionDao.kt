package com.example.daos

import com.example.models.database_representation.QuizQuestionOption
import com.example.models.database_representation.QuizQuestionOptions
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class QuizQuestionOptionDao :
    Dao<QuizQuestionOption>(QuizQuestionOptions) {
    override fun toEntity(row: ResultRow): QuizQuestionOption {
        return QuizQuestionOption(
            id = row[QuizQuestionOptions.id].value,
            text = row[QuizQuestionOptions.text],
            isCorrect = row[QuizQuestionOptions.isCorrect],
        )
    }

    fun add(quizQuestionOption: QuizQuestionOption, quizQuestionId: Int): Int {
        return transaction {
            QuizQuestionOptions.insertAndGetId { row ->
                row[text] = quizQuestionOption.text
                row[isCorrect] = quizQuestionOption.isCorrect
                row[quizQuestion] =
                    quizQuestionId
            }.value
        }
    }

    fun update(id: Int, entity: QuizQuestionOption, quizQuestionId: Int) {
        QuizQuestionOptions.update({ QuizQuestionOptions.id eq id }) { row ->
            row[text] = entity.text
            row[isCorrect] = entity.isCorrect
            row[quizQuestion] =
                quizQuestionId
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