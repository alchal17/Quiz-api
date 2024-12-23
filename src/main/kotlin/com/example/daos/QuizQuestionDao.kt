package com.example.daos

import com.example.models.database_representation.QuizQuestion
import com.example.models.database_representation.QuizQuestions
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class QuizQuestionDao(
    private val quizQuestionOptionDao: QuizQuestionOptionDao,
    private val finder: Finder
) :
    Dao<QuizQuestion>(QuizQuestions) {


    override fun toEntity(row: ResultRow): QuizQuestion {
        val quizId = row[QuizQuestions.quiz].value
        return QuizQuestion(
            id = row[QuizQuestions.id].value,
            quiz = finder.findQuizById(quizId) ?: throw Exception("Quiz not found!"),
            text = row[QuizQuestions.text],
            description = row[QuizQuestions.description],
            imagePath = row[QuizQuestions.imagePath],
            multipleChoices = row[QuizQuestions.multipleChoices],
            options = quizQuestionOptionDao.findByQuizQuestionId(row[QuizQuestions.id].value),
        )
    }

    override fun add(entity: QuizQuestion): Int {
        return transaction {
            val questionId = QuizQuestions.insert { row ->
                row[quiz] = entity.quiz.id ?: throw IllegalArgumentException("Quiz ID is required")
                row[text] = entity.text
                row[description] = entity.description
                row[imagePath] = entity.imagePath
                row[multipleChoices] = entity.multipleChoices
            } get QuizQuestions.id

            val questionOptions = entity.options
            questionOptions.forEach { option ->
                quizQuestionOptionDao.add(option.copy(quizQuestion = entity.copy(id = questionId.value)))
            }

            questionId.value
        }
    }

    override fun update(id: Int, entity: QuizQuestion) {
        transaction {
            QuizQuestions.update({ QuizQuestions.id eq id }) { row ->
                row[quiz] = entity.quiz.id ?: throw IllegalArgumentException("Quiz ID is required")
                row[text] = entity.text
                row[description] = entity.description
                row[imagePath] = entity.imagePath
                row[multipleChoices] = entity.multipleChoices
            }
        }
        entity.options.forEach {
            quizQuestionOptionDao.delete(it)
        }
        entity.options.forEach { option ->
            quizQuestionOptionDao.add(option.copy(quizQuestion = entity.copy(id = id)))
        }
    }

    fun findByQuizId(quizId: Int): List<QuizQuestion> {
        return transaction {
            QuizQuestions.selectAll().where { QuizQuestions.quiz eq quizId }.map { row ->
                toEntity(row)
            }
        }
    }
}