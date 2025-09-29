package com.example.presentation.routes

import com.example.presentation.controllers.QuizController
import com.example.presentation.dto.ApiResponse
import com.example.presentation.dto.Base64QuizDto
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.quizRoutes(quizController: QuizController) {
    route("/quiz") {

//        get("user_id/{id}") {
//            val id = call.parameters["id"]?.toIntOrNull()
//            if (id != null) {
//                call.respond(quizDao.findByUserId(id))
//            } else {
//                call.respond(HttpStatusCode.BadRequest)
//            }
//        }

        get {
            when (val result = quizController.getAll()) {
                is ApiResponse.Failure -> call.respond(HttpStatusCode.BadRequest, result.message)
                is ApiResponse.Success -> call.respond(result.data)
            }
        }

//        get("/base_64_quiz/{id}") {
//            val id = call.parameters["id"]?.toIntOrNull()
//            if (id != null) {
//                val quiz = quizDao.getById(id)
//                if (quiz != null) {
//                    val base64Image = quiz.imagePath?.let {
//                        fileHandler.encodeImageToBase64(it)
//                    }
//                    val base64Quiz = quiz.toBase64Quiz(
//                        base64Image = base64Image
//                    )
//                    call.respond(HttpStatusCode.OK, base64Quiz)
//                }
//                call.respond(HttpStatusCode.NotFound, "Quiz with id $id does not exist")
//            } else {
//                call.respond(HttpStatusCode.BadRequest)
//            }
//        }

//        get("/{id}") {
//            val id = call.parameters["id"]?.toIntOrNull()
//            if (id != null) {
//                val quiz = quizDao.getById(id)
//                if (quiz != null) {
//                    call.respond(quiz)
//                } else {
//                    call.respond(HttpStatusCode.NotFound, "Quiz with id $id does not exist")
//                }
//            } else {
//                call.respond(HttpStatusCode.BadRequest)
//            }
//        }

//        get("/number") {
//            val id = call.parameters["id"]?.toIntOrNull() ?: return@get call.respond(
//                HttpStatusCode.BadRequest,
//                "No ID provided"
//            )
//            call.respond(quizDao.getNumberOfQuestions(id))
//        }

        post {
            val base64QuizDto = call.receive<Base64QuizDto>()

            when (val result = quizController.create(base64QuizDto)) {
                is ApiResponse.Failure -> call.respond(HttpStatusCode.BadRequest, result.message)
                is ApiResponse.Success -> call.respond(result.data)
            }
        }

//        put("/{id}") {
//            val quizId = call.parameters["id"]?.toIntOrNull()
//            if (quizId != null) {
//                val oldQuiz = quizDao.getById(quizId)
//                if (oldQuiz == null) {
//                    call.respond(HttpStatusCode.NotFound, "Quiz with id $quizId does not exist")
//                } else {
//                    val base64QuizDto = call.receive<Base64QuizDto>()
//                    oldQuiz.imagePath?.let { path ->
//                        fileHandler.delete(path)
//                    }
//                    val newFilePath = base64QuizDto.base64Image?.let {
//                        fileHandler.saveImage(it, "/quiz_images")
//                    }
//                    val updatedQuiz = base64QuizDto.toQuiz(newFilePath)
//                    quizDao.update(quizId, updatedQuiz)
//                    call.respond(HttpStatusCode.OK, "Quiz with id $quizId updated")
//                }
//
//            } else {
//                call.respond(HttpStatusCode.BadRequest)
//            }
//        }

        delete("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: return@delete call.respond(
                HttpStatusCode.BadRequest,
                "No id has beed provided"
            )

            when (val result = quizController.delete(id)) {
                is ApiResponse.Failure -> call.respond(HttpStatusCode.BadRequest, result.message)
                is ApiResponse.Success -> call.respond("Quiz with id $id deleted successfully.")
            }

        }

    }
}