package com.example.routing

import com.example.domain.usecase.AddCarUseCase
import com.example.domain.usecase.GetCarsUseCase
import com.google.gson.Gson
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receiveParameters
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.post

fun Route.carRoutes(getCarsUseCase: GetCarsUseCase, addCarUseCase: AddCarUseCase) {
    val gson = Gson()

    post("/get_cars") {
        val p     = call.receiveParameters()
        val phone = p["phone_number"] ?: return@post call.respondText("Missing phone_number", status = HttpStatusCode.BadRequest)
        val cars  = getCarsUseCase(phone) ?: return@post call.respondText("User not found", status = HttpStatusCode.NotFound)
        call.respondText(gson.toJson(cars), ContentType.Application.Json)
    }

    post("/insert_car") {
        try {
            val p     = call.receiveParameters()
            val phone = p["login"] ?: return@post call.respondText("Missing login", status = HttpStatusCode.BadRequest)
            val name  = p["name"]  ?: return@post call.respondText("Missing name",  status = HttpStatusCode.BadRequest)
            val plate = p["plate"] ?: return@post call.respondText("Missing plate", status = HttpStatusCode.BadRequest)
            val vin   = p["VIN"]   ?: return@post call.respondText("Missing VIN",   status = HttpStatusCode.BadRequest)

            when (addCarUseCase(phone, name, plate, vin)) {
                is AddCarUseCase.Result.UserNotFound -> call.respondText("User not found: $phone", status = HttpStatusCode.NotFound)
                is AddCarUseCase.Result.Success      -> call.respondText("ok")
            }
        } catch (e: Exception) {
            println("=== /insert_car ERROR: ${e.message} ===")
            call.respondText("error: ${e.message}", status = HttpStatusCode.InternalServerError)
        }
    }
}
