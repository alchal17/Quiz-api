package com.example.daos

import com.example.files_handlers.BasicFileHandler
import com.example.models.dtos.Quiz
import com.example.models.tables.QuizQuestions
import com.example.models.tables.Quizzes
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update


class QuizDao(
    private val quizQuestionDao: QuizQuestionDao,
    private val fileHandler: BasicFileHandler,
) : Dao<Quiz>(Quizzes) {

    override fun toEntity(row: ResultRow): Quiz {
        return Quiz(
            id = row[Quizzes.id].value,
            name = row[Quizzes.name],
            imagePath = row[Quizzes.imagePath],
            description = row[Quizzes.description],
            userId = row[Quizzes.user].value,
        )
    }

    fun add(quiz: Quiz): Int {
        return transaction {
            val quizId = Quizzes.insert { row ->
                row[name] = quiz.name
                row[user] = quiz.userId
                row[description] = quiz.description
                row[imagePath] = quiz.imagePath
            } get Quizzes.id
            quizId.value
        }
    }

    fun update(id: Int, quiz: Quiz) {
        transaction {
            Quizzes.update({ Quizzes.id eq id }) { row ->
                row[name] = quiz.name
                row[user] = quiz.userId
                row[description] = quiz.description
                row[imagePath] = quiz.imagePath
            }
        }
    }

    fun findByUserId(userId: Int): List<Quiz> {
        return transaction { Quizzes.selectAll().where { Quizzes.user eq userId }.toList().map { toEntity(it) } }
    }

    override fun delete(entity: Quiz) {
        entity.imagePath?.let { imagePath -> fileHandler.delete(imagePath) }
        val quizQuestions = quizQuestionDao.findByQuizId(entity.id ?: throw IllegalArgumentException("Id not found"))
        quizQuestions.forEach { question ->
            question.imagePath?.let { imagePath ->
                fileHandler.delete(imagePath)
            }
        }
        super.delete(entity)
    }

    override fun delete(id: Int) {
        val quiz = getById(id)
        quiz?.imagePath?.let { imagePath ->
            fileHandler.delete(imagePath)
        }
        val quizQuestions = quizQuestionDao.findByQuizId(id)
        quizQuestions.forEach { question ->
            question.imagePath?.let { imagePath ->
                fileHandler.delete(imagePath)
            }
        }
        super.delete(id)
    }

    fun getNumberOfQuestions(quizId: Int): Int {
        return transaction { QuizQuestions.selectAll().where { QuizQuestions.id eq quizId }.count().toInt() }
    }

}