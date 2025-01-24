package com.example.routes

import com.example.daos.QuizDao
import com.example.files_handlers.BasicImageSaver
import com.example.models.request_representation.Base64Quiz
import com.example.models.request_representation.Base64QuizQuestion
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.quizRoutes(quizDao: QuizDao, imageSaver: BasicImageSaver) {
    route("/quiz") {
        post("/create") {
            val base64Quiz = call.receive<Base64Quiz>()
            val base64QuizQuestions = base64Quiz.questions

            val quizQuestions = base64QuizQuestions.map { base64QuizQuestion ->
                val imagePath: String? =
                    if (base64QuizQuestion.base64Image == null) null else imageSaver.saveImage(
                        base64QuizQuestion.base64Image,
                        "/quiz_questions_images"
                    )
                Base64QuizQuestion.toQuizQuestion(base64QuizQuestion, imagePath)
            }
            val base64QuizImagePath =
                if (base64Quiz.base64Image == null) null else imageSaver.saveImage(
                    base64Quiz.base64Image,
                    "/quiz_images"
                )

            val quiz = Base64Quiz.toQuiz(
                base64Quiz = base64Quiz,
                imagePath = base64QuizImagePath,
                quizQuestions = quizQuestions
            )

            call.respond(HttpStatusCode.Created, quizDao.add(quiz = quiz, userId = base64Quiz.userId))
        }

        put("/update") {
            val quizId = call.request.queryParameters["id"]?.toIntOrNull()
            if (quizId != null) {
                val base64Quiz = call.receive<Base64Quiz>()
                val base64QuizQuestions = base64Quiz.questions

                val quizQuestions = base64QuizQuestions.map { base64QuizQuestion ->
                    val imagePath: String? =
                        if (base64QuizQuestion.base64Image == null) null else imageSaver.saveImage(
                            base64QuizQuestion.base64Image,
                            "/quiz_questions_images"
                        )
                    Base64QuizQuestion.toQuizQuestion(base64QuizQuestion, imagePath)
                }
                val base64QuizImagePath =
                    if (base64Quiz.base64Image == null) null else imageSaver.saveImage(
                        base64Quiz.base64Image,
                        "/quiz_images"
                    )

                val quiz = Base64Quiz.toQuiz(
                    base64Quiz = base64Quiz,
                    imagePath = base64QuizImagePath,
                    quizQuestions = quizQuestions
                )

                call.respond(HttpStatusCode.OK, quizDao.update(id = quizId, quiz = quiz, userId = base64Quiz.userId))
            } else {
                call.respond(HttpStatusCode.BadRequest)
            }
        }

        get("/find_by_user_id") {
            val id = call.request.queryParameters["id"]?.toIntOrNull()
            if (id != null) {
                call.respond(quizDao.findByUserId(id))
            } else {
                call.respond(HttpStatusCode.BadRequest)
            }
        }

        get("/all") {
            call.respond(quizDao.getAllDetailedQuizzes())
        }

    }
}