package com.example.domain.model

data class Notification(
    val id: Int = 0,
    val userId: Int,
    val type: String,
    val title: String,
    val body: String,
    val time: String,
    val isRead: Boolean = false
)
