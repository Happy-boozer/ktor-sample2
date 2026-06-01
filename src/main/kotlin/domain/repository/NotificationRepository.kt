package com.example.domain.repository

import com.example.domain.model.Notification

interface NotificationRepository {
    suspend fun findByUserId(userId: Int): List<Notification>
    suspend fun insert(notification: Notification)
    suspend fun markRead(id: Int)
}
