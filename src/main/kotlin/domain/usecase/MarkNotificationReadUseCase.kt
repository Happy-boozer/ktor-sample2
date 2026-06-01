package com.example.domain.usecase

import com.example.domain.repository.NotificationRepository

class MarkNotificationReadUseCase(private val notificationRepository: NotificationRepository) {
    suspend operator fun invoke(id: Int) = notificationRepository.markRead(id)
}
