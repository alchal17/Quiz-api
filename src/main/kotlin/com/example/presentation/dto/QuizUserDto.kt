package com.example.presentation.dto

import com.example.data.models.Model
import com.example.data.models.QuizUser
import com.example.domain.entities.QuizUserEntity
import kotlinx.serialization.Serializable

@Serializable
data class QuizUserDto(
    override val id: Int? = null,
    val username: String,
    val email: String
) : Model

fun QuizUserDto.toEntity(): QuizUserEntity = QuizUserEntity(
    id = id,
    username = username,
    email = email
)
