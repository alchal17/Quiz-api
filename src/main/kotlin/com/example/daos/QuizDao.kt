package com.example.daos

import com.example.files_handlers.BasicFileDeleter
import com.example.models.database_representation.Quiz
import com.example.models.database_representation.Quizzes
import com.example.models.request_representation.DetailedQuiz
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update


class QuizDao(
    private val quizQuestionDao: QuizQuestionDao,
    private val quizUserDao: QuizUserDao,
    private val fileDeleter: BasicFileDeleter
) : Dao<Quiz>(Quizzes) {

    override fun toEntity(row: ResultRow): Quiz {
        return Quiz(
            id = row[Quizzes.id].value,
            name = row[Quizzes.name],
            imagePath = row[Quizzes.imagePath],
            description = row[Quizzes.description],
            questions = quizQuestionDao.findByQuizId(row[Quizzes.id].value)
        )
    }


    private fun toDetailedQuiz(row: ResultRow): DetailedQuiz {
        val user = quizUserDao.get(row[Quizzes.user].value) ?: throw Exception("User not found")
        val quiz = toEntity(row)
        return DetailedQuiz.fromQuizAndAuthor(quiz, user)
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
        quiz.imagePath?.let { imagePath -> fileDeleter.delete(imagePath) }
        transaction {
            Quizzes.update({ Quizzes.id eq id }) { row ->
                row[name] = quiz.name
                row[user] = userId
            }
        }
        quiz.questions.forEach { question ->
            question.imagePath?.let { imagePath -> fileDeleter.delete(imagePath) }
            quizQuestionDao.delete(question)
        }
        quiz.questions.forEach { question ->
            quizQuestionDao.add(
                quizQuestion = question,
                quizId = quiz.id ?: throw Exception("Quiz does not exist"),
            )
        }
    }

    fun findByUserId(userId: Int): List<Quiz> {
        return transaction { Quizzes.selectAll().where { Quizzes.user eq userId }.toList().map { toEntity(it) } }
    }

    fun getAllDetailedQuizzes(): List<DetailedQuiz> = transaction {
        table.selectAll().toList().map { toDetailedQuiz(it) }
    }

}