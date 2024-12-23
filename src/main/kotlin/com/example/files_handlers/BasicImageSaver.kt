package com.example.files_handlers

interface BasicImageSaver {
    //takes image bitmap, path of directory and returns name of a file.
    fun saveImage(bitmap: String, directoryPath: String): String
}