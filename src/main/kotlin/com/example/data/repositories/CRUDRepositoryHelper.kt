package com.example.data.repositories

import com.example.data.models.Model
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

abstract class CRUDRepositoryHelper<T : Model>(protected val table: IntIdTable) {
    protected abstract fun toEntity(row: ResultRow): T
    open fun deleteModel(id: Int): Boolean {
        return try {
            transaction {
                table.deleteWhere { table.id eq id }
            }
            true
        } catch (_: Exception) {
            false
        }
    }

    fun getModelById(id: Int) =
        transaction {
            table.selectAll().where { this@CRUDRepositoryHelper.table.id eq id }.singleOrNull()
        }?.let { toEntity(it) }

    fun getAllModels() = transaction { table.selectAll().toList().map(::toEntity) }

}