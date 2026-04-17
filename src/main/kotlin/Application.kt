package com.example

import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import com.example.data.classes.Car
import com.example.data.classes.User
import com.example.data.tables.Users
import com.example.data.functions.insertUser
import com.example.data.functions.UserbyLoginAndPassword
import io.ktor.server.netty.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.engine.*
import com.example.data.functions.InsertCar
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.selectAll
import io.ktor.server.request.receiveParameters
import com.example.data.functions.UserbyLoginId
import com.example.data.functions.findCarByUserId


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
            //print(users)
            //call.response.headers.append("Content-Type", "application/json")
            call.respond(users)
        }

        post("/gi"){
            val param = call.receiveParameters()
            val usver = User(
                id = 0,
                username = "",
                phone_number = param["phone_number"] ?:"",
                password = param["password"] ?:""
            )
            //println(Users.slice(Users.phone_number).selectALL())

            val user = UserbyLoginAndPassword(usver.phone_number,
                usver.password)
            val password = user?.password
            //call.respondText(usver.password.toString())
            if (password != null){
                call.respondText("ok")
            }
            else{
                call.respondText("notok")
            }

        }


        post("/usver"){
            val param = call.receiveParameters()
            val usver = User(
                id = 0,
                username = param["username"] ?: "",
                phone_number = param["phone_number"] ?:"",
                password = param["password"] ?:""
            )
            usver.password = PasswordHasherSimple.hashPassword(usver.password.toString())
            insertUser(usver)
            call.respondText("user reg.")
        }

        get("/get_cars"){
            val param = call.receiveParameters()
            val usver = User(
                id = 0,
                username = "",
                phone_number = param["phone_number"] ?:"",
                password = param["password"] ?:""
            )
            val user_id = UserbyLoginId(usver.phone_number)!!.id
            val users_cars = findCarByUserId(user_id)
            call.respond(users_cars)

        }

        post("/insertcar"){
            val param = call.receiveParameters()
            val car = Car(
                userId = UserbyLoginId(param["login"] ?: "")!!.id,
                sign = param["plate"] ?:"",
                vin = param["VIN"] ?:"",
                name = param["name"] ?:"",
                status = "2"
            )
            print(car)
            InsertCar(car = car)
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
