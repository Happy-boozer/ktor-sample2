package com.example.domain.usecase

import com.example.domain.model.Notification
import com.example.domain.repository.NotificationRepository
import com.example.domain.repository.UserRepository

class GetNotificationsUseCase(
    private val userRepository: UserRepository,
    private val notificationRepository: NotificationRepository
) {
    suspend operator fun invoke(phone: String): List<Notification>? {
        val user = userRepository.findByPhone(phone) ?: return null
        return notificationRepository.findByUserId(user.id)
    }
}
