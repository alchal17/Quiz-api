package com.example.dto

import com.example.models.Model
import kotlinx.serialization.Serializable

@Serializable
data class QuizUser(override val id: Int? = null, val username: String, val email: String) : Model