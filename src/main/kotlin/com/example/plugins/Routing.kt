package com.example.plugins

import com.example.presentation.controllers.QuizController
import com.example.presentation.controllers.QuizQuestionController
import com.example.presentation.controllers.QuizUserController
import com.example.presentation.routes.imageRoutes
import com.example.presentation.routes.questionRoutes
import com.example.presentation.routes.quizRoutes
import com.example.presentation.routes.userRoutes
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject


fun Application.configureRouting() {

//    val quizUserDao: QuizUserDao by inject()
//    val quizDao: QuizDao by inject()
//    val questionDao: QuizQuestionDao by inject()
//    val optionDao: QuizQuestionOptionDao by inject()
//
//    val fileHandler: FileHandler by inject()
    val quizUserController by inject<QuizUserController>()
    val quizController by inject<QuizController>()
    val quizQuestionController by inject<QuizQuestionController>()

    routing {
        userRoutes(quizUserController)
        quizRoutes(quizController)
        questionRoutes(quizQuestionController)
//        questionOptionRoutes(optionDao)

        imageRoutes()
        get("/") {
            call.respond("Quiz API server runs.")
        }
    }
}
