package com.example.data.models

data class QuizQuestion(
    override val id: Int? = null,
    val quizId: Int,
    val text: String,
    val imagePath: String?,
    val multipleChoices: Boolean,
    val secondsToAnswer: Int,
    val orderNumber: Int
) : Model
