package com.example.data.repositories.quizQuestionRepository

import com.example.data.database.tables.QuizQuestionsTable
import com.example.data.models.QuizQuestion
import com.example.data.repositories.CRUDRepositoryHelper
import org.jetbrains.exposed.sql.ResultRow

class QuizQuestionRepositoryImpl : QuizQuestionRepository, CRUDRepositoryHelper<QuizQuestion>(QuizQuestionsTable) {
    override fun toEntity(row: ResultRow): QuizQuestion {
        return QuizQuestion(
            id = row[QuizQuestionsTable.id].value,
            quizId = row[QuizQuestionsTable.quiz].value,
            text = row[QuizQuestionsTable.text],
            imagePath = row[QuizQuestionsTable.imagePath],
            multipleChoices = row[QuizQuestionsTable.multipleChoices],
            secondsToAnswer = row[QuizQuestionsTable.secondsToAnswer],
            orderNumber = row[QuizQuestionsTable.orderNumber]
        )
    }

    override suspend fun getById(id: Int): QuizQuestion? {
        return super.getModelById(id)
    }

    override suspend fun getAll(): List<QuizQuestion> {
        return super.getAllModels()
    }

    override suspend fun create(entity: QuizQuestion): QuizQuestion? {
        TODO("Not yet implemented")
    }

    override suspend fun update(entity: QuizQuestion): QuizQuestion? {
        TODO("Not yet implemented")
    }

    override suspend fun delete(id: Int): Boolean {
        TODO("Not yet implemented")
    }
}