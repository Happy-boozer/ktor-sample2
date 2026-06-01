package com.example

import com.example.data.repository.CarRepositoryImpl
import com.example.data.repository.NotificationRepositoryImpl
import com.example.data.repository.UserRepositoryImpl
import com.example.domain.usecase.AddCarUseCase
import com.example.domain.usecase.GetCarsUseCase
import com.example.domain.usecase.GetNotificationsUseCase
import com.example.domain.usecase.LoginUseCase
import com.example.domain.usecase.MarkNotificationReadUseCase
import com.example.domain.usecase.RegisterUseCase
import com.example.routing.authRoutes
import com.example.routing.carRoutes
import com.example.routing.notificationRoutes
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.routing

fun main() {
    embeddedServer(Netty, host = "0.0.0.0", port = 3000) {
        module()
    }.start(wait = true)
}

fun Application.module() {
    DatabaseConnector.init()
    install(ContentNegotiation) { json() }

    val userRepo         = UserRepositoryImpl()
    val carRepo          = CarRepositoryImpl()
    val notificationRepo = NotificationRepositoryImpl()

    val loginUseCase               = LoginUseCase(userRepo)
    val registerUseCase            = RegisterUseCase(userRepo)
    val getCarsUseCase             = GetCarsUseCase(userRepo, carRepo)
    val addCarUseCase              = AddCarUseCase(userRepo, carRepo)
    val getNotificationsUseCase    = GetNotificationsUseCase(userRepo, notificationRepo)
    val markNotificationReadUseCase = MarkNotificationReadUseCase(notificationRepo)

    routing {
        authRoutes(loginUseCase, registerUseCase)
        carRoutes(getCarsUseCase, addCarUseCase)
        notificationRoutes(getNotificationsUseCase, markNotificationReadUseCase)
    }
}
