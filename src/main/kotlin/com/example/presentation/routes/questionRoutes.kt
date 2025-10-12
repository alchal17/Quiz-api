package com.example.presentation.routes

import com.example.presentation.controllers.QuizQuestionController
import com.example.presentation.dto.ApiResponse
import com.example.presentation.dto.Base64QuizQuestionDto
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.questionRoutes(quizQuestionController: QuizQuestionController) {
    route("/quiz_question") {
        get {
            when (val result = quizQuestionController.getAll()) {
                is ApiResponse.Failure -> call.respond(HttpStatusCode.BadRequest, result.message)
                is ApiResponse.Success -> call.respond(result.data)
            }
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: return@get call.respond(
                HttpStatusCode.BadRequest,
                "No id has been provided."
            )

            when (val result = quizQuestionController.getById(id)) {
                is ApiResponse.Failure -> call.respond(HttpStatusCode.NotFound, result.message)
                is ApiResponse.Success -> call.respond(result.data)
            }

        }
//
//        get("/find_base64_question_by_id/{id}") {
//            val id = call.parameters["id"]?.toIntOrNull() ?: return@get call.respond(
//                HttpStatusCode.BadRequest,
//                "Invalid id"
//            )
//            val question = questionDao.getById(id) ?: return@get call.respond(
//                HttpStatusCode.NotFound,
//                "Question with id $id not found"
//            )
//            call.respond(question.toBase64QuizQuestion(question.imagePath?.let { fileHandlerRepository.encodeImageToBase64(it) }))
//        }
//
//        get("/find_by_quiz_id/{id}") {
//            val id = call.parameters["id"]?.toIntOrNull()
//            if (id != null) {
//                call.respond(questionDao.findByQuizId(id))
//            } else {
//                call.respond(HttpStatusCode.BadRequest, "Invalid id")
//            }
//        }
//
//        get("/find_base64_questions_by_quiz_id/{id}") {
//            val id = call.parameters["id"]?.toIntOrNull()
//            if (id != null) {
//                call.respond(questionDao.findByQuizId(id).toBase64QuizQuestions(fileHandlerRepository))
//            } else {
//                call.respond(HttpStatusCode.BadRequest, "Invalid id")
//            }
//        }
//
        post {
            val base64Question = call.receive<Base64QuizQuestionDto>()

            when (val result = quizQuestionController.create(base64Question)) {
                is ApiResponse.Failure -> call.respond(HttpStatusCode.BadRequest, result.message)
                is ApiResponse.Success -> call.respond(result.data)
            }
        }
//
//        put("/{id}") {
//            val id = call.parameters["id"]?.toIntOrNull()
//            if (id != null) {
//                val oldQuestion = questionDao.getById(id)
//                if (oldQuestion != null) {
//                    val base64QuizQuestionDto = call.receive<Base64QuizQuestionDto>()
//                    oldQuestion.imagePath?.let {
//                        fileHandlerRepository.delete(it)
//                    }
//                    val newFilePath = base64QuizQuestionDto.base64Image?.let {
//                        fileHandlerRepository.saveImage(it, "/question_images")
//                    }
//                    val updatedQuestion = base64QuizQuestionDto.toQuizQuestion(newFilePath)
//                    questionDao.update(id, updatedQuestion)
//                    call.respond(HttpStatusCode.OK, "Question with id $id successfully updated")
//                } else {
//                    call.respond(HttpStatusCode.NotFound, "Question with id $id not found")
//                }
//            } else {
//                call.respond(HttpStatusCode.BadRequest, "Invalid id")
//            }
//        }
//
//
//        delete("/{id}") {
//            val id = call.parameters["id"]?.toIntOrNull() ?: return@delete call.respond(
//                HttpStatusCode.BadRequest,
//                "Invalid id"
//            )
//            if (!questionDao.existsById(id)) {
//                return@delete call.respond(HttpStatusCode.NotFound, "Question with id $id does not exist")
//            }
//            questionDao.delete(id)
//            call.respond("Question with $id deleted successfully")
//        }
    }
}
