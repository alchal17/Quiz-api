package com.example.data.repositories.quizQuestionRepository

import com.example.data.models.QuizQuestion
import com.example.data.repositories.ModelRepository

interface QuizQuestionRepository: ModelRepository<QuizQuestion> {
    suspend fun getByQuestionId(questionId: Int): List<QuizQuestion>
    suspend fun getByQuizIdAndOrder(quizId: Int, order: Int): QuizQuestion?
}