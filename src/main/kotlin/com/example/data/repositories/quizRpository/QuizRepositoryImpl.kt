package com.example.data.repositories.quizRpository

import com.example.data.database.tables.QuizzesTable
import com.example.data.models.Quiz
import com.example.data.repositories.CRUDRepositoryHelper
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class QuizRepositoryImpl : QuizRepository, CRUDRepositoryHelper<Quiz>(QuizzesTable) {
    override suspend fun findByUserId(userId: Int): List<Quiz> {
        return transaction { QuizzesTable.selectAll().where { QuizzesTable.user eq userId }.map { toEntity(it) } }
    }

    override suspend fun getById(id: Int): Quiz? {
        return super.getModelById(id)
    }

    override suspend fun getAll(): List<Quiz> {
        return super.getAllModels()
    }

    override suspend fun create(entity: Quiz): Quiz? {
        return try {
            transaction {
                val id = QuizzesTable.insertAndGetId { row ->
                    row[name] = entity.name
                    row[user] = entity.userId
                    row[description] = entity.description
                    row[imagePath] = entity.imagePath
                }.value
                entity.copy(id = id)
            }
        } catch (_: Exception) {
            null
        }
    }

    override suspend fun update(entity: Quiz): Quiz? {
        return try {
            val quizId = entity.id ?: return null
            transaction {
                QuizzesTable.update({ QuizzesTable.id eq quizId }) { row ->
                    row[name] = entity.name
                    row[user] = entity.userId
                    row[description] = entity.description
                    row[imagePath] = entity.imagePath
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

    override fun toEntity(row: ResultRow): Quiz {
        return Quiz(
            id = row[QuizzesTable.id].value,
            name = row[QuizzesTable.name],
            userId = row[QuizzesTable.user].value,
            description = row[QuizzesTable.description],
            imagePath = row[QuizzesTable.imagePath]
        )
    }
}