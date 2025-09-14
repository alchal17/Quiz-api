package com.example.data.repositories

interface Repository<T> {
    suspend fun getById(id: Int): T?
    suspend fun getAll(): List<T>
    suspend fun create(entity: T): T?
    suspend fun update(entity: T): T?
    suspend fun delete(id: Int): Boolean
}