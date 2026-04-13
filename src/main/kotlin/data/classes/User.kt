package com.example.data.classes

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int,
    @SerialName("name")
    val username: String,  // соответствует name в БД
    val phone_number: String,  // добавьте
    var password: String?  // добавьте
)
