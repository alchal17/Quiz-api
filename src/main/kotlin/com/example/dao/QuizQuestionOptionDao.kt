package com.example.dao

import com.example.presentation.dto.QuizQuestionOptionDto
import com.example.data.database.tables.QuizQuestionOptionsTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction


class QuizQuestionOptionDao :
    Dao<QuizQuestionOptionDto>(QuizQuestionOptionsTable) {
    override fun toEntity(row: ResultRow): QuizQuestionOptionDto {
        return QuizQuestionOptionDto(
            id = row[QuizQuestionOptionsTable.id].value,
            text = row[QuizQuestionOptionsTable.text],
            isCorrect = row[QuizQuestionOptionsTable.isCorrect],
            quizQuestionId = row[QuizQuestionOptionsTable.quizQuestion].value,
        )
    }

    fun add(quizQuestionOptionDto: QuizQuestionOptionDto): Int {
        return transaction {
            QuizQuestionOptionsTable.insertAndGetId { row ->
                row[text] = quizQuestionOptionDto.text
                row[isCorrect] = quizQuestionOptionDto.isCorrect
                row[quizQuestion] = quizQuestionOptionDto.quizQuestionId
            }.value
        }
    }

    fun addMultiple(quizQuestionOptionDtoList: List<QuizQuestionOptionDto>): List<Int> {
        return transaction {
            quizQuestionOptionDtoList.map { quizQuestionOption ->
                QuizQuestionOptionsTable.insertAndGetId { row ->
                    row[text] = quizQuestionOption.text
                    row[isCorrect] = quizQuestionOption.isCorrect
                    row[quizQuestion] = quizQuestionOption.quizQuestionId
                }.value
            }
        }
    }

    fun update(id: Int, entity: QuizQuestionOptionDto) {
        QuizQuestionOptionsTable.update({ QuizQuestionOptionsTable.id eq id }) { row ->
            row[text] = entity.text
            row[isCorrect] = entity.isCorrect
            row[quizQuestion] = entity.quizQuestionId
        }
    }

    fun findByQuizQuestionId(quizQuestionId: Int): List<QuizQuestionOptionDto> {
        return transaction {
            QuizQuestionOptionsTable.selectAll().where { QuizQuestionOptionsTable.quizQuestion eq quizQuestionId }.map { row ->
                toEntity(row)
            }
        }
   }

    fun replaceQuestionOptions(questionId: Int, newOptions: List<QuizQuestionOptionDto>) {
        transaction {
            QuizQuestionOptionsTable.deleteWhere {
                quizQuestion eq questionId
            }
            newOptions.forEach { option ->
                QuizQuestionOptionsTable.insert {
                    it[text] = option.text
                    it[isCorrect] = option.isCorrect
                    it[quizQuestion] = questionId
                }
            }
        }
    }

}