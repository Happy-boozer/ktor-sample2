package com.example.data.repository

import com.example.data.tables.Notifications
import com.example.domain.model.Notification
import com.example.domain.repository.NotificationRepository
import org.jetbrains.exposed.v1.core.SortOrder
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.v1.jdbc.update

class NotificationRepositoryImpl : NotificationRepository {

    override suspend fun findByUserId(userId: Int): List<Notification> = newSuspendedTransaction {
        Notifications.selectAll()
            .where { Notifications.userId eq userId }
            .orderBy(Notifications.id to SortOrder.DESC)
            .map { it.toNotification() }
    }

    override suspend fun insert(notification: Notification): Unit = newSuspendedTransaction {
        Notifications.insert {
            it[userId] = notification.userId
            it[type]   = notification.type
            it[title]  = notification.title
            it[body]   = notification.body
            it[time]   = notification.time
            it[isRead] = notification.isRead
        }
    }

    override suspend fun markRead(id: Int): Unit = newSuspendedTransaction {
        Notifications.update({ Notifications.id eq id }) { it[isRead] = true }
    }

    private fun org.jetbrains.exposed.v1.core.ResultRow.toNotification() = Notification(
        id     = this[Notifications.id],
        userId = this[Notifications.userId],
        type   = this[Notifications.type],
        title  = this[Notifications.title],
        body   = this[Notifications.body],
        time   = this[Notifications.time],
        isRead = this[Notifications.isRead]
    )
}
