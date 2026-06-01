package com.example.domain.usecase

import com.example.domain.model.Car
import com.example.domain.repository.CarRepository
import com.example.domain.repository.UserRepository

class AddCarUseCase(
    private val userRepository: UserRepository,
    private val carRepository: CarRepository
) {
    sealed class Result {
        object Success : Result()
        object UserNotFound : Result()
    }

    suspend operator fun invoke(phone: String, name: String, plate: String, vin: String): Result {
        val user = userRepository.findByPhone(phone) ?: return Result.UserNotFound
        carRepository.insert(Car(userId = user.id, plate = plate, vin = vin, name = name))
        return Result.Success
    }
}
