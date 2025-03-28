package com.example.daos

import com.example.files_handlers.BasicFileHandler
import com.example.models.dtos.QuizQuestion
import com.example.models.tables.QuizQuestions
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class QuizQuestionDao(
    private val fileHandler: BasicFileHandler
) :
    Dao<QuizQuestion>(QuizQuestions) {


    override fun toEntity(row: ResultRow): QuizQuestion {
        return QuizQuestion(
            id = row[QuizQuestions.id].value,
            text = row[QuizQuestions.text],
            imagePath = row[QuizQuestions.imagePath],
            multipleChoices = row[QuizQuestions.multipleChoices],
            secondsToAnswer = row[QuizQuestions.secondsToAnswer],
            quizId = row[QuizQuestions.quiz].value,
            orderNumber = row[QuizQuestions.orderNumber]
        )
    }

    fun add(quizQuestion: QuizQuestion): Int {
        return transaction {
            val questionId = QuizQuestions.insert { row ->
                row[quiz] = quizQuestion.quizId
                row[text] = quizQuestion.text
                row[imagePath] = quizQuestion.imagePath
                row[multipleChoices] = quizQuestion.multipleChoices
                row[secondsToAnswer] = quizQuestion.secondsToAnswer
                row[orderNumber] = quizQuestion.orderNumber
            } get QuizQuestions.id
            questionId.value
        }
    }

    fun update(id: Int, quizQuestion: QuizQuestion) {
        transaction {
            QuizQuestions.update({ QuizQuestions.id eq id }) { row ->
                row[quiz] = quizQuestion.quizId
                row[text] = quizQuestion.text
                row[imagePath] = quizQuestion.imagePath
                row[multipleChoices] = quizQuestion.multipleChoices
                row[secondsToAnswer] = quizQuestion.secondsToAnswer
                row[orderNumber] = quizQuestion.orderNumber
            }
        }
    }

    fun findByQuizId(quizId: Int): List<QuizQuestion> {
        return transaction {
            QuizQuestions.selectAll().where { QuizQuestions.quiz eq quizId }.map { row ->
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