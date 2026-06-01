package com.example.routing

import com.example.domain.usecase.LoginUseCase
import com.example.domain.usecase.RegisterUseCase
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receiveParameters
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.post

fun Route.authRoutes(loginUseCase: LoginUseCase, registerUseCase: RegisterUseCase) {

    post("/gi") {
        val p = call.receiveParameters()
        val phone    = p["phone_number"] ?: return@post call.respondText("Missing phone_number", status = HttpStatusCode.BadRequest)
        val password = p["password"]     ?: return@post call.respondText("Missing password",     status = HttpStatusCode.BadRequest)
        val ok = loginUseCase(phone, password)
        call.respondText(if (ok) "ok" else "notok")
    }

    post("/usver") {
        val p = call.receiveParameters()
        val name     = p["username"]     ?: return@post call.respondText("Missing username",     status = HttpStatusCode.BadRequest)
        val phone    = p["phone_number"] ?: return@post call.respondText("Missing phone_number", status = HttpStatusCode.BadRequest)
        val password = p["password"]     ?: return@post call.respondText("Missing password",     status = HttpStatusCode.BadRequest)
        registerUseCase(name, phone, password)
        call.respondText("user reg.")
    }
}
