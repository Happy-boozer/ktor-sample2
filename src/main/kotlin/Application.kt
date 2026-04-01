package com.example

import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.engine.*
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.selectAll
import com.example.DatabaseConnector
import io.ktor.server.request.receive
import io.ktor.server.request.receiveParameters
import org.jetbrains.exposed.v1.jdbc.select

fun main(args: Array<String>) {
    EngineMain.main(args)

    /*embeddedServer(Netty, port = 8080){
        module()
        install(ContentNegotiation) {
            json()
        }
        DatabaseConnector.init()
        //configureUserRouting()
    }.start(wait = true)*/
}

object Users: Table(){
    val id = integer("user_id")
    val phone_number = varchar("phone_number", 12)
    val password = varchar("password", 100)
    val name = varchar("name", 100)

    override val primaryKey = PrimaryKey(id)

}

fun Application.configureUserRouting() {
    routing {
        get("/users") {
            val users = transaction {

                Users.selectAll().map {
                    mapOf(
                        "ph_number" to it[Users.phone_number],
                        "name" to it[Users.name],
                        "password" to it[Users.password]
                    )
                }
                //Users.select()
            }
            //call.response.headers.append("Content-Type", "application/json")
            call.respond(users)
        }
        /*get("/user"){
            val User = transaction {
                Users.select(Users.phone_number )
            }
        }*/

        post("/usver"){
            val formParameters = call.receiveParameters()
            val username = formParameters["username"]
            val phone_number = formParameters["phone_number"]
            val password = formParameters["password"]
            if (username != null)
            {
                println("BCrypt хэш: $username")
            }
        }
    }
}
fun Application.module() {
    DatabaseConnector.init()
    install(ContentNegotiation) {
        json()
    }
    configureUserRouting()
}
