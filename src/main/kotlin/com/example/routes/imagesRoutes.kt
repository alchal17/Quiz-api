package com.example.routes

import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respondFile
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
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
            val resolvedFilePath = File(projectBasePath, path)

            if (!resolvedFilePath.exists() || !resolvedFilePath.isFile) {
                return@get call.respondText(
                    "Image not found",
                    status = HttpStatusCode.NotFound
                )
            }

            call.respondFile(resolvedFilePath)
        }
    }
}