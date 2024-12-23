package com.example.daos

import com.example.models.database_representation.Quiz
import com.example.models.database_representation.Quizzes
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update


class QuizDao(private val quizQuestionDao: QuizQuestionDao, private val finder: Finder) : Dao<Quiz>(Quizzes) {


    override fun toEntity(row: ResultRow): Quiz {
        val userId = row[Quizzes.user].value
        return Quiz(
            id = row[Quizzes.id].value,
            name = row[Quizzes.name],
            user = finder.findQuizUserById(userId) ?: throw Exception("User not found"),
            imagePath = row[Quizzes.imagePath],
            questions = quizQuestionDao.findByQuizId(row[Quizzes.id].value)
        )
    }

    override fun add(entity: Quiz): Int {
        return transaction {
            val quizId = Quizzes.insert { row ->
                row[name] = entity.name
                row[user] = entity.user.id ?: throw IllegalArgumentException("User not found")
            } get Quizzes.id

            val questions = entity.questions
            questions.forEach { question ->
                quizQuestionDao.add(question.copy(quiz = entity.copy(id = quizId.value)))
            }
            quizId.value
        }
    }

    override fun update(id: Int, entity: Quiz) {
        transaction {
            Quizzes.update({ Quizzes.id eq id }) { row ->
                row[name] = entity.name
                row[user] = entity.user.id ?: throw IllegalArgumentException("User not found")
            }
        }
        entity.questions.forEach { question ->
            quizQuestionDao.delete(question)
        }
        entity.questions.forEach { question ->
            quizQuestionDao.add(question.copy(quiz = entity.copy(id = id)))
        }
    }
}