package com.example

import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import com.example.data.tables.Users

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        get("/users") {
            val users = transaction {
                Users.selectAll().map {
                    mapOf(
                        "id" to it[Users.id],
                        "name" to it[Users.name]
                    )
                }
            }
            call.response.headers.append("Content-Type", "application/json")
            call.respond(users)
        }

    }

}
