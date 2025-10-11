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

        get {
            when (val result = quizController.getAll()) {
                is ApiResponse.Failure -> call.respond(HttpStatusCode.BadRequest, result.message)
                is ApiResponse.Success -> call.respond(result.data)
            }
        }

        get("/by_user") {
            val userId = call.request.queryParameters["user_id"]?.toIntOrNull()
                ?: return@get call.respond("No user id has been provided.")

            when (val result = quizController.getQuizzesByUserId(userId)) {
                is ApiResponse.Failure -> call.respond(HttpStatusCode.BadRequest, result.message)
                is ApiResponse.Success -> call.respond(result.data)
            }
        }

        get("/base_64_quiz/{id}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: return@get call.respond(
                HttpStatusCode.BadRequest,
                "No id has been provided."
            )

            when (val result = quizController.getBase64QuizById(id)) {
                is ApiResponse.Failure -> call.respond(HttpStatusCode.NotFound, result.message)
                is ApiResponse.Success -> call.respond(result.data)
            }
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: return@get call.respond(
                HttpStatusCode.BadRequest,
                "No id has been provided."
            )

            when (val result = quizController.getById(id)) {
                is ApiResponse.Failure -> call.respond(HttpStatusCode.NotFound, result.message)
                is ApiResponse.Success -> call.respond(result.data)
            }
        }


        post {
            val base64QuizDto = call.receive<Base64QuizDto>()

            when (val result = quizController.create(base64QuizDto)) {
                is ApiResponse.Failure -> call.respond(HttpStatusCode.BadRequest, result.message)
                is ApiResponse.Success -> call.respond(result.data)
            }
        }

        put {
            val quizDto = call.receive<Base64QuizDto>()

            when (val result = quizController.update(quizDto)) {
                is ApiResponse.Failure -> call.respond(HttpStatusCode.BadRequest, result.message)
                is ApiResponse.Success -> call.respond("Quiz ${result.data.id} successfully updated")
            }
        }

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