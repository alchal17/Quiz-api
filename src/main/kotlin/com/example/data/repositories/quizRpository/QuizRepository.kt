package com.example.data.repositories.quizRpository

import com.example.data.models.Quiz
import com.example.data.repositories.ModelRepository

interface QuizRepository: ModelRepository<Quiz> {
    suspend fun findByUserId(userId: Int): List<Quiz>
}