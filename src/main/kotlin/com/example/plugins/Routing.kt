package com.example.plugins

import com.example.daos.QuizDao
import com.example.daos.QuizUserDao
import com.example.files_handlers.BasicImageSaver
import com.example.routes.quizRoutes
import com.example.routes.userRoutes
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject


fun Application.configureRouting() {

    val quizUserDao: QuizUserDao by inject()
    val quizDao: QuizDao by inject()

    val imageSaver: BasicImageSaver by inject()
    routing {
        userRoutes(quizUserDao)
        quizRoutes(quizDao, imageSaver)
        get("/") {
            call.respond("Quiz API server runs.")
        }
    }
}
