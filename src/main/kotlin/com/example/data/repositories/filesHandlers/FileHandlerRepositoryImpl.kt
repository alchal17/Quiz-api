package com.example.data.repositories.filesHandlers

import kotlinx.io.files.FileNotFoundException
import java.io.File
import java.io.FileOutputStream
import java.util.*

class FileHandlerRepositoryImpl : FileHandlerRepository {

    private fun isValidBase64(base64: String): Boolean {
        return try {
            Base64.getDecoder().decode(base64)
            true
        } catch (_: IllegalArgumentException) {
            false
        }
    }

    override fun saveImage(bitmap: String, directoryPath: String): String? {
        if (!isValidBase64(bitmap)) {
            return null
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

    override fun delete(path: String): Boolean {
        val projectBasePath = File("").absolutePath
        val file = File(projectBasePath, path)
        if (file.exists()) {
            file.delete()
            return true
        } else {
            return false
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