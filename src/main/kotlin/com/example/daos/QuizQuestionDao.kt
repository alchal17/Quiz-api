package com.example.daos

import com.example.files_handlers.BasicFileHandler
import com.example.models.database_representation.QuizQuestion
import com.example.models.database_representation.QuizQuestions
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class QuizQuestionDao(
    private val quizQuestionOptionDao: QuizQuestionOptionDao,
    private val fileHandler: BasicFileHandler
) :
    Dao<QuizQuestion>(QuizQuestions) {


    override fun toEntity(row: ResultRow): QuizQuestion {
        return QuizQuestion(
            id = row[QuizQuestions.id].value,
            text = row[QuizQuestions.text],
            imagePath = row[QuizQuestions.imagePath],
            multipleChoices = row[QuizQuestions.multipleChoices],
            options = quizQuestionOptionDao.findByQuizQuestionId(row[QuizQuestions.id].value),
            secondsToAnswer = row[QuizQuestions.secondsToAnswer]
        )
    }

    fun add(quizQuestion: QuizQuestion, quizId: Int): Int {
        return transaction {
            val questionId = QuizQuestions.insert { row ->
                row[quiz] = quizId
                row[text] = quizQuestion.text
                row[imagePath] = quizQuestion.imagePath
                row[multipleChoices] = quizQuestion.multipleChoices
                row[secondsToAnswer] = quizQuestion.secondsToAnswer
            } get QuizQuestions.id

            val questionOptions = quizQuestion.options
            questionOptions.forEach { option ->
                quizQuestionOptionDao.add(option, questionId.value)
            }

            questionId.value
        }
    }

    fun update(id: Int, quizQuestion: QuizQuestion, quizId: Int) {
        transaction {
            QuizQuestions.update({ QuizQuestions.id eq id }) { row ->
                row[quiz] = quizId
                row[text] = quizQuestion.text
                row[imagePath] = quizQuestion.imagePath
                row[multipleChoices] = quizQuestion.multipleChoices
                row[secondsToAnswer] = quizQuestion.secondsToAnswer
            }
        }
        quizQuestion.options.forEach {
            quizQuestionOptionDao.delete(it)
        }
        quizQuestion.options.forEach { option ->
            quizQuestionOptionDao.add(
                quizQuestionOption = option,
                quizQuestionId = quizQuestion.id ?: throw Exception("Question Option Id not found")
            )
        }
    }

    fun findByQuizId(quizId: Int): List<QuizQuestion> {
        return transaction {
            QuizQuestions.selectAll().where { QuizQuestions.quiz eq quizId }.map { row ->
                toEntity(row)
            }
        }
    }

    override fun delete(entity: QuizQuestion) {
        entity.imagePath?.let { imagePath ->
            fileHandler.delete(imagePath)
        }
        super.delete(entity)
    }
}