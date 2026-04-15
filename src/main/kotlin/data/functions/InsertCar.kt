package com.example.data.functions

import com.example.data.classes.Car
import com.example.data.tables.Cars
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

fun InsertCar(car: Car){
    val newCar = transaction {
        Cars.insert {
            it[vin] = car.vin
            it[user_id] = car.userId
            it[sign] = car.sign
            it[name] = car.name
        }
    }
}