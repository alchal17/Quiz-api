package com.example.daos

import com.example.models.dtos.QuizQuestionOption
import com.example.models.tables.QuizQuestionOptions
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class QuizQuestionOptionDao :
    Dao<QuizQuestionOption>(QuizQuestionOptions) {
    override fun toEntity(row: ResultRow): QuizQuestionOption {
        return QuizQuestionOption(
            id = row[QuizQuestionOptions.id].value,
            text = row[QuizQuestionOptions.text],
            isCorrect = row[QuizQuestionOptions.isCorrect],
            quizQuestionId = row[QuizQuestionOptions.quizQuestion].value,
        )
    }

    fun add(quizQuestionOption: QuizQuestionOption): Int {
        return transaction {
            QuizQuestionOptions.insertAndGetId { row ->
                row[text] = quizQuestionOption.text
                row[isCorrect] = quizQuestionOption.isCorrect
                row[quizQuestion] = quizQuestionOption.quizQuestionId
            }.value
        }
    }

    fun addMultiple(quizQuestionOptionList: List<QuizQuestionOption>): List<Int> {
        return transaction {
            quizQuestionOptionList.map {quizQuestionOption ->
                QuizQuestionOptions.insertAndGetId { row ->
                    row[text] = quizQuestionOption.text
                    row[isCorrect] = quizQuestionOption.isCorrect
                    row[quizQuestion] = quizQuestionOption.quizQuestionId
                }.value
            }
        }
    }

    fun update(id: Int, entity: QuizQuestionOption) {
        QuizQuestionOptions.update({ QuizQuestionOptions.id eq id }) { row ->
            row[text] = entity.text
            row[isCorrect] = entity.isCorrect
            row[quizQuestion] = entity.quizQuestionId
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