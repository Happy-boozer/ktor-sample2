package com.example.domain.usecase

import com.example.domain.model.User
import com.example.domain.repository.UserRepository
import com.example.PasswordHasherSimple

class RegisterUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(name: String, phone: String, password: String) {
        val hashed = PasswordHasherSimple.hashPassword(password)
        userRepository.insert(User(name = name, phoneNumber = phone, password = hashed))
    }
}
