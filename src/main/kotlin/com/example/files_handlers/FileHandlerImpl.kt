package com.example.files_handlers

import kotlinx.io.files.FileNotFoundException
import java.io.File
import java.io.FileOutputStream
import java.util.*

class FileHandlerImpl : FileHandler {

    private fun isValidBase64(base64: String): Boolean {
        return try {
            Base64.getDecoder().decode(base64)
            true
        } catch (_: IllegalArgumentException) {
            false
        }
    }

    override fun saveImage(bitmap: String, directoryPath: String): String {
        if (!isValidBase64(bitmap)) {
            throw IllegalArgumentException("Invalid Base64 string")
        }

        val decodedBytes = Base64.getDecoder().decode(bitmap)

        val fileName = "${UUID.randomUUID()}.png"

        val projectBasePath = File("").absolutePath
        val resolvedDirectoryPath = File(projectBasePath, directoryPath)


        if (!resolvedDirectoryPath.exists()) {
            resolvedDirectoryPath.mkdirs()
        }

        val filePath = File(resolvedDirectoryPath, fileName)

        FileOutputStream(filePath).use { outputStream ->
            outputStream.write(decodedBytes)
        }

        return File(directoryPath, fileName).path

    }

    override fun delete(path: String) {
        val projectBasePath = File("").absolutePath
        val file = File(projectBasePath, path)
        if (file.exists()) {
            file.delete()
        } else {
            throw Exception("File does not exist")
        }

    }

    override fun encodeImageToBase64(imagePath: String): String {
        val projectBasePath = File("").absolutePath
        val absoluteImagePath = File(projectBasePath, imagePath)

        if (!absoluteImagePath.exists()) {
            throw FileNotFoundException("File not found: ${absoluteImagePath.path}")
        }

        val imageBytes = absoluteImagePath.readBytes()
        return Base64.getEncoder().encodeToString(imageBytes)    }

}