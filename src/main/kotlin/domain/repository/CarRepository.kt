package com.example.domain.repository

import com.example.domain.model.Car

interface CarRepository {
    suspend fun findByUserId(userId: Int): List<Car>
    suspend fun insert(car: Car)
}
