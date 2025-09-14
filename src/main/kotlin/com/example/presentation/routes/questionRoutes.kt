package com.example.presentation.routes

import com.example.dao.QuizQuestionDao
import com.example.presentation.dto.Base64QuizQuestionDto
import com.example.presentation.dto.toBase64QuizQuestion
import com.example.presentation.dto.toBase64QuizQuestions
import com.example.presentation.dto.toQuizQuestion
import com.example.files_handlers.FileHandler
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.questionRoutes(questionDao: QuizQuestionDao, fileHandler: FileHandler) {
    route("/quiz_question") {
        get {
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

        get("/find_base64_question_by_id/{id}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: return@get call.respond(
                HttpStatusCode.BadRequest,
                "Invalid id"
            )
            val question = questionDao.getById(id) ?: return@get call.respond(
                HttpStatusCode.NotFound,
                "Question with id $id not found"
            )
            call.respond(question.toBase64QuizQuestion(question.imagePath?.let { fileHandler.encodeImageToBase64(it) }))
        }

        get("/find_by_quiz_id/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id != null) {
                call.respond(questionDao.findByQuizId(id))
            } else {
                call.respond(HttpStatusCode.BadRequest, "Invalid id")
            }
        }

        get("/find_base64_questions_by_quiz_id/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id != null) {
                call.respond(questionDao.findByQuizId(id).toBase64QuizQuestions(fileHandler))
            } else {
                call.respond(HttpStatusCode.BadRequest, "Invalid id")
            }
        }

        post {
            val base64Question = call.receive<Base64QuizQuestionDto>()
            val filePath = base64Question.base64Image?.let {
                fileHandler.saveImage(it, "/question_images")
            }
            val question = base64Question.toQuizQuestion(filePath)
            call.respond(HttpStatusCode.Created, questionDao.add(question))
        }

        put("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id != null) {
                val oldQuestion = questionDao.getById(id)
                if (oldQuestion != null) {
                    val base64QuizQuestionDto = call.receive<Base64QuizQuestionDto>()
                    oldQuestion.imagePath?.let {
                        fileHandler.delete(it)
                    }
                    val newFilePath = base64QuizQuestionDto.base64Image?.let {
                        fileHandler.saveImage(it, "/question_images")
                    }
                    val updatedQuestion = base64QuizQuestionDto.toQuizQuestion(newFilePath)
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
