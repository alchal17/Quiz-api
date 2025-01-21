package com.example.routes

import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File

fun Route.imageRoutes() {
    route("/images") {
        get("/get_image") {
            val path = call.request.queryParameters["path"]
                ?: return@get call.respondText(
                    "Image path not provided",
                    status = HttpStatusCode.BadRequest
                )
            val projectBasePath = File("").absolutePath
            val filePath = File(projectBasePath, path)

            if (!filePath.exists() || !filePath.isFile) {
                return@get call.respondText(
                    "Image not found",
                    status = HttpStatusCode.NotFound
                )
            }

            call.respondFile(filePath)

        }
    }
}