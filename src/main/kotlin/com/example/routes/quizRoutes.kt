package com.example.routes

import com.example.daos.QuizDao
import com.example.files_handlers.BasicFileHandler
import com.example.models.dtos.Base64Quiz
import com.example.models.dtos.toBase64Quiz
import com.example.models.dtos.toQuiz
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.quizRoutes(quizDao: QuizDao, fileHandler: BasicFileHandler) {
    route("/quiz") {

        get("/find_by_user_id") {
            val id = call.request.queryParameters["id"]?.toIntOrNull()
            if (id != null) {
                call.respond(quizDao.findByUserId(id))
            } else {
                call.respond(HttpStatusCode.BadRequest)
            }
        }

        get("/all") {
            call.respond(quizDao.getAll())
        }

        get("/find_base64_quiz_by_id") {
            val id = call.request.queryParameters["id"]?.toIntOrNull()
            if (id != null) {
                val quiz = quizDao.getById(id)
                if (quiz != null) {
                    val base64Image = quiz.imagePath?.let {
                        fileHandler.encodeImageToBase64(it)
                    }
                    val base64Quiz = quiz.toBase64Quiz(
                        base64Image = base64Image
                    )
                    call.respond(HttpStatusCode.OK, base64Quiz)
                }
                call.respond(HttpStatusCode.NotFound, "Quiz with id $id does not exist")
            } else {
                call.respond(HttpStatusCode.BadRequest)
            }
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id != null) {
                val quiz = quizDao.getById(id)
                if (quiz != null) {
                    call.respond(quiz)
                } else {
                    call.respond(HttpStatusCode.NotFound, "Quiz with id $id does not exist")
                }
            } else {
                call.respond(HttpStatusCode.BadRequest)
            }
        }

        get("/get_questions_number") {
            val id = call.parameters["id"]?.toIntOrNull() ?: return@get call.respond(
                HttpStatusCode.BadRequest,
                "No ID provided"
            )
            call.respond(quizDao.getNumberOfQuestions(id))
        }

        post("/create") {
            val base64Quiz = call.receive<Base64Quiz>()
            val filePath = base64Quiz.base64Image?.let {
                fileHandler.saveImage(it, "/quiz_images")
            }
            val quiz = base64Quiz.toQuiz(filePath)
            call.respond(HttpStatusCode.Created, quizDao.add(quiz))
        }

        put("/update/{id}") {
            val quizId = call.parameters["id"]?.toIntOrNull()
            if (quizId != null) {
                val oldQuiz = quizDao.getById(quizId)
                if (oldQuiz == null) {
                    call.respond(HttpStatusCode.NotFound, "Quiz with id $quizId does not exist")
                } else {
                    val base64Quiz = call.receive<Base64Quiz>()
                    oldQuiz.imagePath?.let { path ->
                        fileHandler.delete(path)
                    }
                    val newFilePath = base64Quiz.base64Image?.let {
                        fileHandler.saveImage(it, "/quiz_images")
                    }
                    val updatedQuiz = base64Quiz.toQuiz(newFilePath)
                    quizDao.update(quizId, updatedQuiz)
                    call.respond(HttpStatusCode.OK, "Quiz with id $quizId updated")
                }

            } else {
                call.respond(HttpStatusCode.BadRequest)
            }
        }

        delete("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Pass a valid id")
                return@delete
            }
            try {
                quizDao.delete(id)
                call.respond(HttpStatusCode.OK, "Quiz with id $id successfully deleted")
            } catch (_: NoSuchElementException) {
                call.respond(HttpStatusCode.NotFound, "Quiz with id $id not found")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "An error occurred: ${e.message}")
            }
        }

    }
}