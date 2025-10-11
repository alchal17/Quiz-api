package com.example.domain.entities

data class QuizQuestionEntity(
    val id: Int? = null,
    val quizId: Int,
    val text: String,
    val imagePath: String?,
    val multipleChoices: Boolean,
    val secondsToAnswer: Int,
    val orderNumber: Int
)
