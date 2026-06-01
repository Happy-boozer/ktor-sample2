package com.example.domain.usecase

import com.example.domain.model.Car
import com.example.domain.repository.CarRepository
import com.example.domain.repository.UserRepository

class GetCarsUseCase(
    private val userRepository: UserRepository,
    private val carRepository: CarRepository
) {
    suspend operator fun invoke(phone: String): List<Car>? {
        val user = userRepository.findByPhone(phone) ?: return null
        return carRepository.findByUserId(user.id)
    }
}
