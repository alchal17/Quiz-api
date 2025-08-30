package com.example.dao

import com.example.dto.QuizQuestionOption
import com.example.models.tables.QuizQuestionOptions
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction


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

    fun replaceQuestionOptions(questionId: Int, newOptions: List<QuizQuestionOption>) {
        transaction {
            QuizQuestionOptions.deleteWhere {
                quizQuestion eq questionId
            }
            newOptions.forEach { option ->
                QuizQuestionOptions.insert {
                    it[text] = option.text
                    it[isCorrect] = option.isCorrect
                    it[quizQuestion] = questionId
                }
            }
        }
    }

}