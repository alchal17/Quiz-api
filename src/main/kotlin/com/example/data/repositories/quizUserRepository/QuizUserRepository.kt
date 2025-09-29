package com.example.data.repositories.quizUserRepository

import com.example.data.models.QuizUser
import com.example.data.repositories.ModelRepository

interface QuizUserRepository : ModelRepository<QuizUser> {
    suspend fun findByUsername(username: String): QuizUser?
    suspend fun findByEmail(email: String): QuizUser?
    suspend fun findAnotherUserByUsername(id: Int, username: String): QuizUser?
    suspend fun findAnotherUserByEmail(id: Int, email: String): QuizUser?
}