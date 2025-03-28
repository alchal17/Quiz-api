package com.example.routes

import com.example.daos.QuizQuestionDao
import com.example.files_handlers.BasicFileHandler
import com.example.models.dtos.Base64QuizQuestion
import com.example.models.dtos.toBase64QuizQuestion
import com.example.models.dtos.toBase64QuizQuestions
import com.example.models.dtos.toQuizQuestion
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.questionRoutes(questionDao: QuizQuestionDao, fileHandler: BasicFileHandler) {
    route("/quiz_question") {
        get("/all") {
            call.respond(questionDao.getAll())
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id != null) {
                val question = questionDao.getById(id)
                if (question != null) {
                    call.respond(HttpStatusCode.OK, question)
                } else {
                    call.respond(HttpStatusCode.NotFound, "Question with id $id not found")
                }
            } else {
                call.respond(HttpStatusCode.BadRequest, "Invalid id")
            }
        }

        get("/find_base64_question_by_id") {
            val id = call.request.queryParameters["id"]?.toIntOrNull() ?: return@get call.respond(
                HttpStatusCode.BadRequest,
                "Invalid id"
            )
            val question = questionDao.getById(id) ?: return@get call.respond(
                HttpStatusCode.NotFound,
                "Question with id $id not found"
            )
            call.respond(question.toBase64QuizQuestion(question.imagePath?.let { fileHandler.encodeImageToBase64(it) }))
        }

        get("/find_by_quiz_id") {
            val id = call.request.queryParameters["id"]?.toIntOrNull()
            if (id != null) {
                call.respond(questionDao.findByQuizId(id))
            } else {
                call.respond(HttpStatusCode.BadRequest, "Invalid id")
            }
        }

        get("/find_base64_questions_by_quiz_id") {
            val id = call.request.queryParameters["id"]?.toIntOrNull()
            if (id != null) {
                call.respond(questionDao.findByQuizId(id).toBase64QuizQuestions(fileHandler))
            } else {
                call.respond(HttpStatusCode.BadRequest, "Invalid id")
            }
        }

        post("/create") {
            val base64Question = call.receive<Base64QuizQuestion>()
            val filePath = base64Question.base64Image?.let {
                fileHandler.saveImage(it, "/question_images")
            }
            val question = base64Question.toQuizQuestion(filePath)
            call.respond(HttpStatusCode.Created, questionDao.add(question))
        }

        put("/update/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id != null) {
                val oldQuestion = questionDao.getById(id)
                if (oldQuestion != null) {
                    val base64QuizQuestion = call.receive<Base64QuizQuestion>()
                    oldQuestion.imagePath?.let {
                        fileHandler.delete(it)
                    }
                    val newFilePath = base64QuizQuestion.base64Image?.let {
                        fileHandler.saveImage(it, "/question_images")
                    }
                    val updatedQuestion = base64QuizQuestion.toQuizQuestion(newFilePath)
                    questionDao.update(id, updatedQuestion)
                    call.respond(HttpStatusCode.OK, "Question with id $id successfully updated")
                } else {
                    call.respond(HttpStatusCode.NotFound, "Question with id $id not found")
                }
            } else {
                call.respond(HttpStatusCode.BadRequest, "Invalid id")
            }
        }

        delete("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: return@delete call.respond(
                HttpStatusCode.BadRequest,
                "Invalid id"
            )
            if (!questionDao.existsById(id)) {
                return@delete call.respond(HttpStatusCode.NotFound, "Question with id $id does not exist")
            }
            questionDao.delete(id)
            call.respond("Question with $id deleted successfully")
        }
    }
}
