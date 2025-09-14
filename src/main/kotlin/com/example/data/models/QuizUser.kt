package com.example.data.models

import com.example.domain.entities.QuizUserEntity

data class QuizUser(
    override val id: Int? = null,
    val username: String,
    val email: String
) : Model

fun QuizUser.toQuizUserEntity(): QuizUserEntity = QuizUserEntity(
    id = id,
    username = username,
    email = email
)
