package com.example.data.models

data class Quiz(
    override val id: Int? = null,
    val name: String,
    val userId: Int,
    val description: String?,
    val imagePath: String?
) : Model