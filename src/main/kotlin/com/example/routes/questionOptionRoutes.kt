package com.example.routes

import com.example.daos.QuizQuestionOptionDao
import com.example.models.dtos.QuizQuestionOption
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.questionOptionRoutes(quizQuestionOptionDao: QuizQuestionOptionDao) {
    route("/quiz_question_option") {
        get("/all") {
            call.respond(quizQuestionOptionDao.getAll())
        }

        get("/test"){
            call.respond("test")
        }

        get("/{id}") {
            val id =
                call.parameters["id"]?.toIntOrNull() ?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid id")

            val option = quizQuestionOptionDao.getById(id) ?: return@get call.respond(
                HttpStatusCode.NotFound,
                "Question option with id $id does not exist"
            )
            call.respond(HttpStatusCode.OK, option)
        }

        get("/find_by_question_id") {
            val id = call.request.queryParameters["id"]?.toIntOrNull() ?: return@get call.respond(
                HttpStatusCode.BadRequest,
                "Invalid id"
            )
            call.respond(HttpStatusCode.OK, quizQuestionOptionDao.findByQuizQuestionId(id))
        }

        post("/create") {
            val quizQuestionOption = call.receive<QuizQuestionOption>()
            call.respond(HttpStatusCode.Created, quizQuestionOptionDao.add(quizQuestionOption))
        }

        post("/create_multiple") {
            val quizQuestionOptions = call.receive<List<QuizQuestionOption>>()
            call.respond(HttpStatusCode.Created, quizQuestionOptionDao.addMultiple(quizQuestionOptions))
        }

        put("/update/{id}") {
            val id =
                call.parameters["id"]?.toIntOrNull() ?: return@put call.respond(HttpStatusCode.BadRequest, "Invalid id")
            if (!quizQuestionOptionDao.existsById(id)) {
                return@put call.respond(HttpStatusCode.NotFound, "Question option with id $id does not exist")
            }
            val option = call.receive<QuizQuestionOption>()
            quizQuestionOptionDao.update(id, option)
            call.respond("Question option with id $id updated successfully")
        }

        delete("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: return@delete call.respond(
                HttpStatusCode.BadRequest,
                "Invalid id"
            )
            if (!quizQuestionOptionDao.existsById(id)) {
                return@delete call.respond(HttpStatusCode.NotFound, "Question option with id $id does not exist")
            }
            quizQuestionOptionDao.delete(id)
        }
    }
}