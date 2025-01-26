package com.example.files_handlers

interface BasicFileHandler {
    //takes image bitmap, path of directory and returns name of a file.
    fun saveImage(bitmap: String, directoryPath: String): String

    fun delete(path: String)

    fun encodeImageToBase64(imagePath: String): String
}