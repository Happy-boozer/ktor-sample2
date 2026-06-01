package com.example.data.repository

import com.example.data.tables.Cars
import com.example.domain.model.Car
import com.example.domain.repository.CarRepository
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.experimental.newSuspendedTransaction

class CarRepositoryImpl : CarRepository {

    override suspend fun findByUserId(userId: Int): List<Car> = newSuspendedTransaction {
        Cars.selectAll()
            .where { Cars.user_id eq userId }
            .map { it.toCar() }
    }

    override suspend fun insert(car: Car): Unit = newSuspendedTransaction {
        Cars.insert {
            it[user_id] = car.userId
            it[sighn]   = car.plate
            it[vin]     = car.vin
            it[name]    = car.name
            it[satatus] = car.status.firstOrNull().toString()
        }
    }

    private fun org.jetbrains.exposed.v1.core.ResultRow.toCar() = Car(
        id     = this[Cars.id],
        userId = this[Cars.user_id],
        plate  = this[Cars.sighn],
        vin    = this[Cars.vin],
        name   = this[Cars.name],
        status = this[Cars.satatus]
    )
}
