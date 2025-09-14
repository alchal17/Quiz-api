package com.example.presentation.dto

sealed interface ApiResponse<out T> {
    data class Success<T>(val data: T) : ApiResponse<T>
    data class Failure(val message: String) : ApiResponse<Nothing>
}