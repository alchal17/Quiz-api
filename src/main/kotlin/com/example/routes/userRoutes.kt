package com.example.routes

import com.example.daos.QuizUserDao
import com.example.models.dtos.QuizUser
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.userRoutes(quizUserDao: QuizUserDao) {
    route("/quiz_user") {
        get("/get_by_email") {
            val email = call.request.queryParameters["email"]
            if (email != null) {
                val user = quizUserDao.getUserByEmail(email)
                if (user != null) {
                    call.respond(HttpStatusCode.OK, user)
                } else {
                    call.respond(HttpStatusCode.NotFound, "User not found")
                }
            } else {
                call.respond(HttpStatusCode.BadRequest, "Please enter a valid email")
            }
        }

        get("/get_by_username") {
            val username = call.request.queryParameters["username"]
            if (username != null) {
                val user = quizUserDao.getUserByUsername(username)
                if (user != null) {
                    call.respond(HttpStatusCode.OK, user)
                } else {
                    call.respond(HttpStatusCode.NotFound, "User not found")
                }
            } else {
                call.respond(HttpStatusCode.BadRequest, "Please enter a valid username")
            }
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id != null) {
                val user = quizUserDao.getById(id)
                if (user != null) {
                    call.respond(status = HttpStatusCode.OK, message = user)
                } else {
                    call.respond(status = HttpStatusCode.NotFound, message = "User not found")
                }
            } else {
                call.respond(status = HttpStatusCode.BadRequest, message = "Please enter a valid id")
            }
        }

        get("/all") {
            call.respond(quizUserDao.getAll())
        }

        post("/create") {
            val user = call.receive<QuizUser>()
            if (quizUserDao.getUserByEmail(user.email) != null) {
                call.respond(HttpStatusCode.Conflict, "User with this email already exists")
            } else if (quizUserDao.getUserByUsername(user.username) != null) {
                call.respond(HttpStatusCode.Conflict, "User with this username already exists")
            } else {
                call.respond(HttpStatusCode.Created, quizUserDao.add(user))
            }
        }

        put("/update/{id}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: return@put call.respond(
                HttpStatusCode.BadRequest,
                "Please enter a valid id"
            )
            if (!quizUserDao.existsById(id)) {
                return@put call.respond(HttpStatusCode.NotFound, "User with id $id does not exist")
            }
            val user = call.receive<QuizUser>()
            quizUserDao.update(id, user)
            call.respond("User with id $id updated successfully")
        }

        delete("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: return@delete call.respond(
                HttpStatusCode.BadRequest,
                "Please enter a valid id"
            )
            if (!quizUserDao.existsById(id)) {
                return@delete call.respond(HttpStatusCode.NotFound, "User with id $id not found")
            }
            quizUserDao.delete(id)
            call.respond("User with id $id successfully deleted")
        }

    }
}
