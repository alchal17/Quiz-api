package com.example.domain.usecases.image

import com.example.data.repositories.filesHandlers.FileHandlerRepository

class SaveImageUseCase(private val fileHandlerRepository: FileHandlerRepository) {
    private val directoryPath = "/quiz_images"

    operator fun invoke(base64Image: String): Result<String> {
        return when(val result = fileHandlerRepository.saveImage(base64Image, directoryPath)) {
            null -> Result.failure(Exception("Error saving the image"))
            else -> Result.success(result)
        }
    }
}