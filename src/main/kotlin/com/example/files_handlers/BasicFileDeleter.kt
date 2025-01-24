package com.example.files_handlers

import java.io.File

class FileDeleter: BasicFileDeleter {
    override fun delete(path: String) {
        val file = File(path)
        if (file.exists()) {
            file.delete()
        } else {
            throw Exception("File does not exist")
        }
    }
}