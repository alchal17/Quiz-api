package com.example.domain.entities

import com.example.data.models.QuizQuestion
import com.example.presentation.dto.QuizQuestionDto

data class QuizQuestionEntity(
    val id: Int? = null,
    val quizId: Int,
    val text: String,
    val imagePath: String?,
    val multipleChoices: Boolean,
    val secondsToAnswer: Int,
    val orderNumber: Int
)

fun QuizQuestionEntity.toQuizQuestionDto(): QuizQuestionDto = QuizQuestionDto(
    id = id,
    quizId = quizId,
    text = text,
    imagePath = imagePath,
    multipleChoices = multipleChoices,
    secondsToAnswer = secondsToAnswer,
    orderNumber = orderNumber
)

fun QuizQuestionEntity.toQuizQuestion(): QuizQuestion = QuizQuestion(
    id = id,
    quizId = quizId,
    text = text,
    imagePath = imagePath,
    multipleChoices = multipleChoices,
    secondsToAnswer = secondsToAnswer,
    orderNumber = orderNumber
)