package com.example.daos

import com.example.models.database_representation.Quiz
import com.example.models.database_representation.Quizzes
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update


class QuizDao(private val quizQuestionDao: QuizQuestionDao) : Dao<Quiz>(Quizzes) {
    override fun toEntity(row: ResultRow): Quiz {
        return Quiz(
            id = row[Quizzes.id].value,
            name = row[Quizzes.name],
            imagePath = row[Quizzes.imagePath],
            description = row[Quizzes.description],
            questions = quizQuestionDao.findByQuizId(row[Quizzes.id].value)
        )
    }

    fun add(quiz: Quiz, userId: Int): Int {
        return transaction {
            val quizId = Quizzes.insert { row ->
                row[name] = quiz.name
                row[user] = userId
                row[description] = quiz.description
                row[imagePath] = quiz.imagePath
            } get Quizzes.id

            val questions = quiz.questions
            questions.forEach { question ->
                quizQuestionDao.add(
                    quizQuestion = question,
                    quizId = quizId.value
                )
            }
            quizId.value
        }
    }

    fun update(id: Int, quiz: Quiz, userId: Int) {
        transaction {
            Quizzes.update({ Quizzes.id eq id }) { row ->
                row[name] = quiz.name
                row[user] = userId
            }
        }
        quiz.questions.forEach { question ->
            quizQuestionDao.delete(question)
        }
        quiz.questions.forEach { question ->
            quizQuestionDao.add(
                quizQuestion = question,
                quizId = quiz.id ?: throw Exception("Quiz does not exist"),
            )
        }
    }
}