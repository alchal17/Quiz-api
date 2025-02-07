package com.example.plugins

import com.example.daos.QuizDao
import com.example.daos.QuizQuestionDao
import com.example.daos.QuizQuestionOptionDao
import com.example.daos.QuizUserDao
import com.example.files_handlers.BasicFileHandler
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

    val fileHandler: BasicFileHandler by inject()
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
