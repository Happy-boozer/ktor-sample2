package com.example.data.classes

import kotlinx.serialization.Serializable

@Serializable
data class Car(
    var userId: Int,
    var sign: String,
    var vin: String,
    var name: String,
    val status: String
)
