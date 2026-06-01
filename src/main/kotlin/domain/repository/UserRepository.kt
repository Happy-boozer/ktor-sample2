package com.example.domain.repository

import com.example.domain.model.User

interface UserRepository {
    suspend fun findByPhone(phone: String): User?
    suspend fun findByPhoneAndPassword(phone: String, password: String): User?
    suspend fun insert(user: User)
}
