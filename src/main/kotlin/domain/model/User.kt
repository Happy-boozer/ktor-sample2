package com.example.domain.model

data class User(
    val id: Int = 0,
    val name: String,
    val phoneNumber: String,
    val password: String = ""
)
