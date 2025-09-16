package com.example.data.repositories.quizUserRepository

import com.example.data.database.tables.QuizUsersTable
import com.example.data.models.QuizUser
import com.example.data.repositories.CRUDRepositoryHelper
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class QuizUserRepositoryImpl : CRUDRepositoryHelper<QuizUser>(QuizUsersTable), QuizUserRepository {
    override fun toEntity(row: ResultRow): QuizUser {
        return QuizUser(
            id = row[QuizUsersTable.id].value,
            username = row[QuizUsersTable.username],
            email = row[QuizUsersTable.email]
        )
    }

    override suspend fun findByUsername(username: String): QuizUser? {
        return transaction {
            QuizUsersTable.selectAll().where { QuizUsersTable.username eq username }.map { toEntity(it) }.firstOrNull()
        }

    }

    override suspend fun findByEmail(email: String): QuizUser? {
        return transaction {
            QuizUsersTable.selectAll().where { QuizUsersTable.email eq email }.map { toEntity(it) }.firstOrNull()
        }

    }

    override suspend fun findAnotherUserByUsername(
        id: Int,
        username: String
    ): QuizUser? {
        return transaction {
            QuizUsersTable.selectAll().where { (QuizUsersTable.username eq username) and (QuizUsersTable.id neq id) }
                .map { toEntity(it) }.firstOrNull()
        }
    }

    override suspend fun findAnotherUserByEmail(
        id: Int,
        email: String
    ): QuizUser? {
        return transaction {
            QuizUsersTable.selectAll().where { (QuizUsersTable.email eq email) and (QuizUsersTable.id neq id) }
                .map { toEntity(it) }.firstOrNull()
        }
    }

    override suspend fun getById(id: Int): QuizUser? {
        return super.getModelById(id)
    }

    override suspend fun getAll(): List<QuizUser> {
        return super.getAllModels()
    }

    override suspend fun create(entity: QuizUser): QuizUser? {
        return try {
            transaction {
                val id = QuizUsersTable.insertAndGetId { row ->
                    row[username] = entity.username
                    row[email] = entity.email
                }.value
                entity.copy(id = id)
            }
        } catch (_: Exception) {
            null
        }
    }

    override suspend fun update(entity: QuizUser): QuizUser? {
        return try {
            val userId = entity.id ?: return null
            transaction {
                QuizUsersTable.update({ QuizUsersTable.id eq userId }) { row ->
                    row[email] = entity.email
                    row[username] = entity.username
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

}