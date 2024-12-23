package com.example.files_handlers

import java.io.File
import java.io.FileOutputStream
import java.util.*

class ImageSaver : BasicImageSaver {
    override fun saveImage(bitmap: String, directoryPath: String): String {

        val decodedBytes = Base64.getDecoder().decode(bitmap)

        val fileName = "${UUID.randomUUID()}.png"
        val filePath = "$directoryPath/$fileName"

        val directory = File(directoryPath)
        if (!directory.exists()) {
            directory.mkdirs()
        }

        val imageFile = File(filePath)
        FileOutputStream(imageFile).use { outputStream ->
            outputStream.write(decodedBytes)
        }

        return fileName
    }
}