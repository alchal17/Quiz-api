package com.example.presentation.routes

import com.example.presentation.controllers.QuizUserController
import com.example.presentation.dto.ApiResponse
import com.example.presentation.dto.QuizUserDto
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.userRoutes(quizUserController: QuizUserController) {
    route("/quiz_user") {
        get("/email/{email}") {
            val email = call.parameters["email"] ?: return@get call.respond(
                HttpStatusCode.BadRequest,
                "No email has been provided."
            )
            when (val result = quizUserController.findUserByEmail(email)) {
                is ApiResponse.Failure -> call.respond(HttpStatusCode.NotFound, result.message)
                is ApiResponse.Success -> call.respond(result.data)
            }
        }

        get("/username/{name}") {
            val username = call.parameters["name"] ?: return@get call.respond(
                HttpStatusCode.BadRequest,
                "No username has been provided."
            )
            when (val result = quizUserController.findByUsername(username)) {
                is ApiResponse.Failure -> call.respond(HttpStatusCode.NotFound, result.message)
                is ApiResponse.Success -> call.respond(result.data)
            }
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: return@get call.respond(
                status = HttpStatusCode.BadRequest,
                message = "Please enter a valid id"
            )
            when (val result = quizUserController.getById(id)) {
                is ApiResponse.Failure -> call.respond(HttpStatusCode.NotFound, result.message)
                is ApiResponse.Success -> call.respond(result.data)
            }
        }

        get {
            when (val response = quizUserController.getAll()) {
                is ApiResponse.Failure -> call.respond(HttpStatusCode.BadRequest, response.message)
                is ApiResponse.Success -> call.respond(response.data)
            }
        }

        post {
            val userDto = call.receive<QuizUserDto>()
            when (val result = quizUserController.create(userDto)) {
                is ApiResponse.Failure -> call.respond(HttpStatusCode.Conflict, result.message)
                is ApiResponse.Success -> call.respond(HttpStatusCode.Created, result.data)
            }
        }

        put {
            val quizUserDto = call.receive<QuizUserDto>()

            when (val result = quizUserController.update(quizUserDto)) {
                is ApiResponse.Failure -> call.respond(HttpStatusCode.Conflict, result.message)
                is ApiResponse.Success -> call.respond(result.data)
            }
        }

        delete("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: return@delete call.respond(
                HttpStatusCode.BadRequest,
                "Please enter a valid id."
            )

            when (val result = quizUserController.delete(id)) {
                is ApiResponse.Failure -> call.respond(HttpStatusCode.BadRequest, result.message)
                is ApiResponse.Success -> call.respond("User with id $id deleted successfully.")
            }
        }

    }
}
