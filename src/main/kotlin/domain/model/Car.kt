package com.example.domain.model

data class Car (
     val id: Int = 0,
    val userId: Int,
     val plate: String,
     val vin: String,
     val name: String,
     val status: String = "Активный"
)