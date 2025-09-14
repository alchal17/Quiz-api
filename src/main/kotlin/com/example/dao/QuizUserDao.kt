package com.example.dao

import com.example.presentation.dto.QuizUserDto
import com.example.data.database.tables.QuizUsersTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class QuizUserDao : Dao<QuizUserDto>(table = QuizUsersTable) {
    override fun toEntity(row: ResultRow): QuizUserDto {
        return QuizUserDto(id = row[QuizUsersTable.id].value, username = row[QuizUsersTable.username], email = row[QuizUsersTable.email])
    }

    fun add(entity: QuizUserDto): Int {
        return transaction {
            QuizUsersTable.insertAndGetId { row ->
                row[username] = entity.username
                row[email] = entity.email
            }.value
        }
    }

    fun update(id: Int, entity: QuizUserDto) {
        transaction {
            QuizUsersTable.update({ QuizUsersTable.id eq id }) { row ->
                row[username] = entity.username
                row[email] = entity.email
            }
        }
    }

    fun getUserByEmail(email: String): QuizUserDto? {
        return transaction {
            QuizUsersTable.selectAll().where { QuizUsersTable.email eq email }.map { toEntity(it) }.firstOrNull()
        }
    }

    fun getUserByUsername(username: String): QuizUserDto? {
        return transaction {
            QuizUsersTable.selectAll().where { QuizUsersTable.username eq username }.map { toEntity(it) }.firstOrNull()
        }
    }

    fun getByEmailOrUsername(email: String, username: String): QuizUserDto? {
        return transaction {
            QuizUsersTable.selectAll().where { (QuizUsersTable.email eq email) or (QuizUsersTable.username eq username) }
                .map { toEntity(it) }
                .firstOrNull()
        }
    }
}