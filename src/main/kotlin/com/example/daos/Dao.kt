package com.example.daos

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
        transaction { table.deleteWhere { table.id eq id } }
    }

    open fun delete(entity: T) {
        transaction { table.deleteWhere { table.id eq entity.id } }
    }


    fun getById(id: Int) =
        transaction { table.selectAll().where { this@Dao.table.id eq id }.singleOrNull() }?.let { toEntity(it) }

    fun getAll() = transaction { table.selectAll().toList().map(::toEntity) }
}