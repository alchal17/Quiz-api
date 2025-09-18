package com.example.data.repositories.quizRpository

import com.example.data.models.Quiz
import com.example.data.repositories.Repository

interface QuizRepository: Repository<Quiz> {
    suspend fun findByUserId(userId: Int): List<Quiz>
}