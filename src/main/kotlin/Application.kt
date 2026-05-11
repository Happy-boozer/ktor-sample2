package com.example

import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
//import com.example.data.classes.Car
import com.google.gson.Gson
import com.example.data.classes.User
import com.example.data.tables.Users
import com.example.data.functions.insertUser
import com.example.data.functions.UserbyLoginAndPassword
import io.ktor.server.netty.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import com.example.data.functions.InsertCar
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.selectAll
import io.ktor.server.request.receiveParameters
import com.example.data.functions.UserbyLoginId
import com.example.data.functions.findCarByUserId
import io.ktor.http.HttpStatusCode
import com.example.data.classes.Car
import io.ktor.http.ContentType


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
            //call.respond(users)
            call.respondText("https://gtzed0-95-24-167-178.ru.tuna.am")
        }

        post("/gi"){
            val param = call.receiveParameters()
            print(param)
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
                //print("ok")
                call.respondText("ok")
            }
            else{
                call.respondText("notok")
                /*print("" +
                        "noto")*/
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

        post("/get_cars") {

                println("=== POST /get_cars called ===")

                // Получаем параметры из тела POST запроса
                val parameters = call.receiveParameters()
                val phoneNumber = parameters["phone_number"]

                println("Phone number from POST body: $phoneNumber")

                if (phoneNumber.isNullOrEmpty()) {
                    println("ERROR: Missing phone_number parameter")
                    call.respondText(
                        "Missing phone_number parameter",
                        status = HttpStatusCode.BadRequest
                    )
                    return@post
                }

                // Находим пользователя по номеру телефона
                val user = UserbyLoginId(phoneNumber)
                println("User found: $user")

                if (user == null) {
                    println("User not found for phone: $phoneNumber")
                    call.respondText(
                        "User not found",
                        status = HttpStatusCode.NotFound
                    )
                    return@post
                }

                // Находим машины пользователя
                val usersCars: List<Car> = findCarByUserId(user.id)
                println(usersCars)
                println("Found ${usersCars.size} cars for user ${user.id}")


                // Возвращаем список машин
            val gson = Gson()
            val jsonString = gson.toJson(usersCars)
                call.respondText(jsonString, ContentType.Application.Json)


        }

        /*get("/get_cars"){
            // Для GET параметры получаем через call.request.queryParameters
            val loginValue = call.request.queryParameters["phone_number"]
            print("${89},${loginValue}")

            if (loginValue == null) {
                call.respondText("Missing phone_number parameter", status = HttpStatusCode.BadRequest)
                return@get
            }

            val user_id = UserbyLoginId(loginValue)?.id
            if (user_id == null) {
                call.respondText("User not found", status = HttpStatusCode.NotFound)
                return@get
            }

            val users_cars = findCarByUserId(user_id)
            call.respond(users_cars)
            print(users_cars)
        }*/

        post("/insert_car"){
            val param = call.receiveParameters()
            print(param)
            val pattern = Regex("value=(\\d+)")
            val loginValue = param["login"].toString()
            //val value = pattern.find(param["login"] ?: "")?.groupValues?.get(1).toString()
            val car = Car(
                userId = UserbyLoginId(loginValue)!!.id,
                sighn = param["plate"] ?:"",
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
