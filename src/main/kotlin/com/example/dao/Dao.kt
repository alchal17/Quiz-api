package com.example.dao

import com.example.models.Model
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

abstract class Dao<T : Model>(protected val table: IntIdTable) {
    protected abstract fun toEntity(row: ResultRow): T
    open fun delete(id: Int) {
        transaction {
            table.deleteWhere { table.id eq id }
        }
    }

    open fun delete(entity: T) {
        transaction {
            val exists = !table.selectAll().where { table.id eq entity.id }.empty()
            if (exists) {
                table.deleteWhere { table.id eq id }
            } else {
                throw NoSuchElementException("Entity with id $id not found")
            }
        }

    }

    fun getById(id: Int) =
        transaction { table.selectAll().where { this@Dao.table.id eq id }.singleOrNull() }?.let { toEntity(it) }

    fun getAll() = transaction { table.selectAll().toList().map(::toEntity) }

    fun existsById(id: Int): Boolean {
        return transaction { !table.selectAll().where { table.id eq id }.empty() }
    }
}