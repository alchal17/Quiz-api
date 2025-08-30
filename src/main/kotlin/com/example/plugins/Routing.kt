package com.example.plugins

import com.example.dao.QuizDao
import com.example.dao.QuizQuestionDao
import com.example.dao.QuizQuestionOptionDao
import com.example.dao.QuizUserDao
import com.example.files_handlers.FileHandler
import com.example.routes.imageRoutes
import com.example.routes.questionOptionRoutes
import com.example.routes.questionRoutes
import com.example.routes.quizRoutes
import com.example.routes.userRoutes
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject


fun Application.configureRouting() {

    val quizUserDao: QuizUserDao by inject()
    val quizDao: QuizDao by inject()
    val questionDao: QuizQuestionDao by inject()
    val optionDao: QuizQuestionOptionDao by inject()

    val fileHandler: FileHandler by inject()
    routing {
        userRoutes(quizUserDao)
        quizRoutes(quizDao, fileHandler)
        questionRoutes(questionDao, fileHandler)
        questionOptionRoutes(optionDao)

        imageRoutes()
        get("/") {
            call.respond("Quiz API server runs.")
        }
    }
}
