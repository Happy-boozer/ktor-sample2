package com.example.routing

import com.example.domain.usecase.GetNotificationsUseCase
import com.example.domain.usecase.MarkNotificationReadUseCase
import com.google.gson.Gson
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receiveParameters
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.post

fun Route.notificationRoutes(
    getNotificationsUseCase: GetNotificationsUseCase,
    markNotificationReadUseCase: MarkNotificationReadUseCase
) {
    val gson = Gson()

    post("/get_notifications") {
        val p     = call.receiveParameters()
        val phone = p["phone_number"] ?: return@post call.respondText("Missing phone_number", status = HttpStatusCode.BadRequest)
        val list  = getNotificationsUseCase(phone) ?: return@post call.respondText("User not found", status = HttpStatusCode.NotFound)
        call.respondText(gson.toJson(list), ContentType.Application.Json)
    }

    post("/mark_read") {
        val p  = call.receiveParameters()
        val id = p["id"]?.toIntOrNull() ?: return@post call.respondText("Missing or invalid id", status = HttpStatusCode.BadRequest)
        markNotificationReadUseCase(id)
        call.respondText("ok")
    }
}
