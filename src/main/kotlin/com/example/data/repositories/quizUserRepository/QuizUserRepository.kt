package com.example.data.repositories.quizUserRepository

import com.example.data.models.QuizUser
import com.example.data.repositories.Repository

interface QuizUserRepository: Repository<QuizUser> {
    suspend fun findByUsername(username: String): QuizUser?
    suspend fun findByEmail(email: String): QuizUser?
}