package com.example.data.repositories.filesHandlers

interface FileHandlerRepository {
    //takes image bitmap, path of directory and returns name of a file.
    fun saveImage(bitmap: String, directoryPath: String): String?

    fun delete(path: String): Boolean

    fun encodeImageToBase64(imagePath: String): String
}