package com.example.data.functions

import com.example.data.tables.Cars
import com.example.data.classes.Car
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.transactions.experimental.newSuspendedTransaction

suspend fun findCarByUserId(UserId: Int): List<Car> = newSuspendedTransaction {
    Cars.selectAll().
    where{ Cars.user_id eq UserId}.map{
        Car(
            name = it[Cars.name],
            vin = it[Cars.vin],
            userId = it[Cars.user_id],
            sign = it[Cars.sign],
            status = it[Cars.satatus]
        )
    }
}