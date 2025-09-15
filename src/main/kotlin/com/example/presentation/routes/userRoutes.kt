package com.example.presentation.routes

import com.example.presentation.controllers.QuizUserController
import com.example.presentation.dto.ApiResponse
import com.example.presentation.dto.QuizUserDto
import io.ktor.http.*
import io.ktor.server.request.receive
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.userRoutes(quizUserController: QuizUserController) {
    route("/quiz_user") {
//        get("/email/{email}") {
//            val email = call.parameters["email"]
//            if (email != null) {
//                val user = quizUserDao.getUserByEmail(email)
//                if (user != null) {
//                    call.respond(HttpStatusCode.OK, user)
//                } else {
//                    call.respond(HttpStatusCode.NotFound, "User not found")
//                }
//            } else {
//                call.respond(HttpStatusCode.BadRequest, "Please enter a valid email")
//            }
//        }

//        get("/username/{name}") {
//            val username = call.parameters["name"]
//            if (username != null) {
//                val user = quizUserDao.getUserByUsername(username)
//                if (user != null) {
//                    call.respond(HttpStatusCode.OK, user)
//                } else {
//                    call.respond(HttpStatusCode.NotFound, "User not found")
//                }
//            } else {
//                call.respond(HttpStatusCode.BadRequest, "Please enter a valid username")
//            }
//        }

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

//        put("/{id}") {
//            val id = call.parameters["id"]?.toIntOrNull() ?: return@put call.respond(
//                HttpStatusCode.BadRequest,
//                "Please enter a valid id"
//            )
//            if (!quizUserDao.existsById(id)) {
//                return@put call.respond(HttpStatusCode.NotFound, "User with id $id does not exist")
//            }
//            val user = call.receive<QuizUserDto>()
//            quizUserDao.update(id, user)
//            call.respond("User with id $id updated successfully")
//        }

        delete("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: return@delete call.respond(
                HttpStatusCode.BadRequest,
                "Please enter a valid id."
            )

            when(val result = quizUserController.delete(id)){
                is ApiResponse.Failure -> call.respond(HttpStatusCode.BadRequest, result.message)
                is ApiResponse.Success -> call.respond("User with id $id deleted successfully.")
            }
        }

    }
}
