package com.example.data.functions

import com.example.data.tables.Cars
import com.example.data.classes.Car
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.experimental.newSuspendedTransaction

suspend fun findCarByUserId(UserId: Int): List<Car> = newSuspendedTransaction {
    Cars.selectAll().filter{ UserId.equals(Cars.user_id) }.map{
        Car(
            userId = it[Cars.user_id],
            sign = it[Cars.sign],
            status = it[Cars.satatus]
        )
    }
}