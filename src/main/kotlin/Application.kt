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
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.request.receiveParameters
import io.ktor.server.request.receiveText
import org.jetbrains.exposed.v1.jdbc.select
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.SerialName
import com.example.PasswordHasherSimple
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.transactions.experimental.newSuspendedTransaction


@Serializable
data class User(
    //val id: Int,
    @SerialName("name")
    val username: String,  // соответствует name в БД
    val phone_number: String,  // добавьте
    var password: String  // добавьте
)

@Serializable
data class Car(
    var userId: Int,
    var sign: String,
    var status: String
)

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

object Cars: Table(){
    val id = integer("car_id")
    val sign = varchar("sign", 9)
    val user_id = integer("user_id")
    val satatus = varchar("status", 15)
}

suspend fun findCarByUserId(UserId: Int): List<Car> = newSuspendedTransaction {
    Cars.selectAll().filter{ UserId.equals(Cars.user_id) }.map{
        Car(
            userId = it[Cars.user_id],
            sign = it[Cars.sign],
            status = it[Cars.satatus]
        )
    }
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

        get("/suser"){
            val cars = transaction {

                Cars.selectAll().map{
                    mapOf(
                        "UserID" to it[Cars.user_id]
                    )
                }
            }
        }


        post("/usver"){
            val param = call.receiveParameters()
            val usver = User(
                username = param["username"] ?: "",
                phone_number = param["phone_number"] ?:"",
                password = param["password"] ?:""
            )
            usver.password = PasswordHasherSimple.hashPassword(usver.password)

            val newUser = transaction {
                Users.insert {
                    it[name] = usver.username
                    it[phone_number] = usver.phone_number
                    it[password] = usver.password
                }
            }
            call.respondText("user reg. $newUser.")
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
