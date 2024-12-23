package com.example.daos

import com.example.models.database_representation.*
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class Finder {
    private fun rowToQuizUser(row: ResultRow): QuizUser? {
        return QuizUser(id = row[QuizUsers.id].value, username = row[QuizUsers.username], email = row[QuizUsers.email])
    }

    private fun rowToQuiz(row: ResultRow): Quiz {
        val userId = row[Quizzes.user].value
        return Quiz(
            id = row[Quizzes.id].value,
            name = row[Quizzes.name],
            user = findQuizUserById(userId) ?: throw Exception("User not found"),
            imagePath = row[Quizzes.imagePath],
            questions = findQuestionsByQuizId(row[Quizzes.id].value)
        )

    }

    private fun rowToQuizQuestion(row: ResultRow): QuizQuestion? {
        val quizId = row[QuizQuestions.quiz].value
        return QuizQuestion(
            id = row[QuizQuestions.id].value,
            quiz = findQuizById(quizId) ?: throw Exception("Quiz not found!"),
            text = row[QuizQuestions.text],
            description = row[QuizQuestions.description],
            imagePath = row[QuizQuestions.imagePath],
            multipleChoices = row[QuizQuestions.multipleChoices],
            options = findOptionsByQuizQuestionId(row[QuizQuestions.id].value),
        )
    }

    private fun rowToQuizQuestionOption(row: ResultRow): QuizQuestionOption {
        val quizQuestionId = row[QuizQuestionOptions.quizQuestion].value
        return QuizQuestionOption(
            id = row[QuizQuestionOptions.id].value,
            text = row[QuizQuestionOptions.text],
            isCorrect = row[QuizQuestionOptions.isCorrect],
            quizQuestion = findQuizQuestionById(quizQuestionId) ?: throw Exception("Unknown quiz question id"),
        )

    }

    private fun findQuestionsByQuizId(quizId: Int): List<QuizQuestion> {
        return transaction {
            QuizQuestions.selectAll().where { QuizQuestions.quiz eq quizId }.map { row ->
                rowToQuizQuestion(row) ?: throw Exception("Quiz not found")
            }
        }
    }


    private fun findOptionsByQuizQuestionId(id: Int): List<QuizQuestionOption> {
        return transaction {
            QuizQuestionOptions.selectAll().where { QuizQuestionOptions.quizQuestion eq id }.map { row ->
                rowToQuizQuestionOption(row)
            }
        }

    }

    fun findQuizUserById(id: Int): QuizUser? {
        return transaction {
            QuizUsers.selectAll().where { QuizUsers.id eq id }.singleOrNull()
        }?.let { rowToQuizUser(it) }
    }

    fun findQuizQuestionById(id: Int): QuizQuestion? {
        return transaction {
            QuizQuestions.selectAll().where { QuizQuestions.id eq id }.singleOrNull()
        }?.let { rowToQuizQuestion(it) }

    }

    fun findQuizById(id: Int): Quiz? {
        return transaction {
            Quizzes.selectAll().where { Quizzes.id eq id }.singleOrNull()
        }?.let { rowToQuiz(it) }

    }
}