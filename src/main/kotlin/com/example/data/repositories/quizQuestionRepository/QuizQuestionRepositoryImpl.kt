package com.example.data.repositories.quizQuestionRepository

import com.example.data.database.tables.QuizQuestionsTable
import com.example.data.models.QuizQuestion
import com.example.data.repositories.CRUDRepositoryHelper
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class QuizQuestionRepositoryImpl : QuizQuestionRepository, CRUDRepositoryHelper<QuizQuestion>(QuizQuestionsTable) {
    override fun toEntity(row: ResultRow): QuizQuestion {
        return QuizQuestion(
            id = row[QuizQuestionsTable.id].value,
            quizId = row[QuizQuestionsTable.quiz].value,
            text = row[QuizQuestionsTable.text],
            imagePath = row[QuizQuestionsTable.imagePath],
            multipleChoices = row[QuizQuestionsTable.multipleChoices],
            secondsToAnswer = row[QuizQuestionsTable.secondsToAnswer],
            orderNumber = row[QuizQuestionsTable.orderNumber]
        )
    }

    override suspend fun getById(id: Int): QuizQuestion? {
        return super.getModelById(id)
    }

    override suspend fun getAll(): List<QuizQuestion> {
        return super.getAllModels()
    }

    override suspend fun create(entity: QuizQuestion): QuizQuestion? {
        return try {
            transaction {
                val id = QuizQuestionsTable.insertAndGetId { row ->
                    row[quiz] = entity.quizId
                    row[text] = entity.text
                    row[imagePath] = entity.imagePath
                    row[multipleChoices] = entity.multipleChoices
                    row[secondsToAnswer] = entity.secondsToAnswer
                    row[orderNumber] = entity.orderNumber
                }.value
                entity.copy(id = id)
            }
        } catch (_: Exception) {
            null
        }
    }

    override suspend fun update(entity: QuizQuestion): QuizQuestion? {
        return try {
            val quizQuestionId = entity.id ?: return null
            transaction {
                QuizQuestionsTable.update({ QuizQuestionsTable.id eq quizQuestionId }) { row ->
                    row[quiz] = entity.quizId
                    row[text] = entity.text
                    row[imagePath] = entity.imagePath
                    row[multipleChoices] = entity.multipleChoices
                    row[secondsToAnswer] = entity.secondsToAnswer
                    row[orderNumber] = entity.orderNumber
                }
                entity
            }
        } catch (_: Exception) {
            null
        }

    }

    override suspend fun delete(id: Int): Boolean {
        return super.deleteModel(id)
    }

    override suspend fun getByQuestionId(questionId: Int): List<QuizQuestion> {
        return transaction {
            QuizQuestionsTable.selectAll().where { QuizQuestionsTable.quiz eq questionId }.map { toEntity(it) }
        }
    }

    override suspend fun getByQuizIdAndOrder(
        quizId: Int,
        order: Int
    ): QuizQuestion? {
        return transaction {
            QuizQuestionsTable.selectAll()
                .where { (QuizQuestionsTable.quiz eq quizId) and (QuizQuestionsTable.orderNumber eq order) }
                .singleOrNull()
                ?.let { toEntity(it) }
        }
    }
}