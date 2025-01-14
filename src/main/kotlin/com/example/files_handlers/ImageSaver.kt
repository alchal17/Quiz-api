package com.example.files_handlers

import java.io.File
import java.io.FileOutputStream
import java.util.*



class ImageSaver : BasicImageSaver {
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

        // Return relative path
        return File(directoryPath, fileName).path
    }

}