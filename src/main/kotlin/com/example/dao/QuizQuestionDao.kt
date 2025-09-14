package com.example.dao

import com.example.files_handlers.FileHandler
import com.example.presentation.dto.QuizQuestionDto
import com.example.data.database.tables.QuizQuestionsTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update


class QuizQuestionDao(
    private val fileHandler: FileHandler
) :
    Dao<QuizQuestionDto>(QuizQuestionsTable) {


    override fun toEntity(row: ResultRow): QuizQuestionDto {
        return QuizQuestionDto(
            id = row[QuizQuestionsTable.id].value,
            text = row[QuizQuestionsTable.text],
            imagePath = row[QuizQuestionsTable.imagePath],
            multipleChoices = row[QuizQuestionsTable.multipleChoices],
            secondsToAnswer = row[QuizQuestionsTable.secondsToAnswer],
            quizId = row[QuizQuestionsTable.quiz].value,
            orderNumber = row[QuizQuestionsTable.orderNumber]
        )
    }

    fun add(quizQuestionDto: QuizQuestionDto): Int {
        return transaction {
            val questionId = QuizQuestionsTable.insert { row ->
                row[quiz] = quizQuestionDto.quizId
                row[text] = quizQuestionDto.text
                row[imagePath] = quizQuestionDto.imagePath
                row[multipleChoices] = quizQuestionDto.multipleChoices
                row[secondsToAnswer] = quizQuestionDto.secondsToAnswer
                row[orderNumber] = quizQuestionDto.orderNumber
            } get QuizQuestionsTable.id
            questionId.value
        }
    }

    fun update(id: Int, quizQuestionDto: QuizQuestionDto) {
        transaction {
            QuizQuestionsTable.update({ QuizQuestionsTable.id eq id }) { row ->
                row[quiz] = quizQuestionDto.quizId
                row[text] = quizQuestionDto.text
                row[imagePath] = quizQuestionDto.imagePath
                row[multipleChoices] = quizQuestionDto.multipleChoices
                row[secondsToAnswer] = quizQuestionDto.secondsToAnswer
                row[orderNumber] = quizQuestionDto.orderNumber
            }
        }
    }

    fun findByQuizId(quizId: Int): List<QuizQuestionDto> {
        return transaction {
            QuizQuestionsTable.selectAll().where { QuizQuestionsTable.quiz eq quizId }.map { row ->
                toEntity(row)
            }
        }
    }

    override fun delete(id: Int) {
        val question = getById(id)
        question?.imagePath?.let {
            fileHandler.delete(it)
        }

        super.delete(id)
    }
}