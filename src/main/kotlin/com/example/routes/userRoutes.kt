package com.example.routes

import com.example.daos.QuizUserDao
import com.example.models.database_representation.QuizUser
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

        get("/get_by_id") {
            val id = call.request.queryParameters["id"]?.toIntOrNull()
            if (id != null) {
                val user = quizUserDao.get(id)
                if (user != null) {
                    call.respond(status = HttpStatusCode.OK, message = user)
                } else {
                    call.respond(status = HttpStatusCode.NotFound, message = "User not found")
                }
            } else {
                call.respond(status = HttpStatusCode.BadRequest, message = "Please enter a valid id")
            }
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
    }
}
