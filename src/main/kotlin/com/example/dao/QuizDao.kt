package com.example.dao

import com.example.data.repositories.filesHandlers.FileHandlerRepository
import com.example.presentation.dto.QuizDto
import com.example.data.database.tables.QuizQuestionsTable
import com.example.data.database.tables.QuizzesTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update


class QuizDao(
    private val quizQuestionDao: QuizQuestionDao,
    private val fileHandlerRepository: FileHandlerRepository,
) : Dao<QuizDto>(QuizzesTable) {

    override fun toEntity(row: ResultRow): QuizDto {
        return QuizDto(
            id = row[QuizzesTable.id].value,
            name = row[QuizzesTable.name],
            imagePath = row[QuizzesTable.imagePath],
            description = row[QuizzesTable.description],
            userId = row[QuizzesTable.user].value,
        )
    }

    fun add(quizDto: QuizDto): Int {
        return transaction {
            val quizId = QuizzesTable.insert { row ->
                row[name] = quizDto.name
                row[user] = quizDto.userId
                row[description] = quizDto.description
                row[imagePath] = quizDto.imagePath
            } get QuizzesTable.id
            quizId.value
        }
    }

    fun update(id: Int, quizDto: QuizDto) {
        transaction {
            QuizzesTable.update({ QuizzesTable.id eq id }) { row ->
                row[name] = quizDto.name
                row[user] = quizDto.userId
                row[description] = quizDto.description
                row[imagePath] = quizDto.imagePath
            }
        }
    }

    fun findByUserId(userId: Int): List<QuizDto> {
        return transaction { QuizzesTable.selectAll().where { QuizzesTable.user eq userId }.toList().map { toEntity(it) } }
    }

    override fun delete(entity: QuizDto) {
        entity.imagePath?.let { imagePath -> fileHandlerRepository.delete(imagePath) }
        val quizQuestions = quizQuestionDao.findByQuizId(entity.id ?: throw IllegalArgumentException("Id not found"))
        quizQuestions.forEach { question ->
            question.imagePath?.let { imagePath ->
                fileHandlerRepository.delete(imagePath)
            }
        }
        super.delete(entity)
    }

    override fun delete(id: Int) {
        val quiz = getById(id)
        quiz?.imagePath?.let { imagePath ->
            fileHandlerRepository.delete(imagePath)
        }
        val quizQuestions = quizQuestionDao.findByQuizId(id)
        quizQuestions.forEach { question ->
            question.imagePath?.let { imagePath ->
                fileHandlerRepository.delete(imagePath)
            }
        }
        super.delete(id)
    }

    fun getNumberOfQuestions(quizId: Int): Int {
        return transaction { QuizQuestionsTable.selectAll().where { QuizQuestionsTable.id eq quizId }.count().toInt() }
    }

}