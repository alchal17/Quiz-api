package com.example.data.models

import com.example.domain.entities.QuizQuestionEntity

data class QuizQuestion(
    override val id: Int? = null,
    val quizId: Int,
    val text: String,
    val imagePath: String?,
    val multipleChoices: Boolean,
    val secondsToAnswer: Int,
    val orderNumber: Int
) : Model

fun QuizQuestion.toQuizQuestionEntity(): QuizQuestionEntity = QuizQuestionEntity(
    id = id,
    quizId = quizId,
    text = text,
    imagePath = imagePath,
    multipleChoices = multipleChoices,
    secondsToAnswer = secondsToAnswer,
    orderNumber = orderNumber
)