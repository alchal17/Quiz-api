package com.example.daos

import com.example.models.database_representation.QuizUser
import com.example.models.database_representation.QuizUsers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class QuizUserDao : Dao<QuizUser>(table = QuizUsers) {
    override fun toEntity(row: ResultRow): QuizUser {
        return QuizUser(id = row[QuizUsers.id].value, username = row[QuizUsers.username], email = row[QuizUsers.email])
    }

    override fun add(entity: QuizUser): Int {
        return transaction {
            QuizUsers.insertAndGetId { row ->
                row[username] = entity.username
                row[email] = entity.email
            }.value
        }
    }

    override fun update(id: Int, entity: QuizUser) {
        transaction {
            QuizUsers.update({ QuizUsers.id eq id }) { row ->
                row[username] = entity.username
                row[email] = entity.email
            }
        }
    }

    fun getUserByEmail(email: String): QuizUser? {
        return transaction {
            QuizUsers.selectAll().where { QuizUsers.email eq email }.map { toEntity(it) }.firstOrNull()
        }
    }

    fun getUserByUsername(username: String): QuizUser? {
        return transaction {
            QuizUsers.selectAll().where { QuizUsers.email eq username }.map { toEntity(it) }.firstOrNull()
        }
    }

    fun getByEmailOrUsername(email: String, username: String): QuizUser? {
        return transaction {
            QuizUsers.selectAll().where { (QuizUsers.email eq email) or (QuizUsers.username eq username) }
                .map { toEntity(it) }
                .firstOrNull()
        }
    }
}