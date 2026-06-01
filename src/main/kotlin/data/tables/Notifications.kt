package com.example.data.tables

import org.jetbrains.exposed.v1.core.Table

object Notifications : Table("notifications") {
    val id       = integer("id").autoIncrement()
    val userId   = integer("user_id")
    val type     = varchar("type", 10)
    val title    = varchar("title", 100)
    val body     = varchar("body", 500)
    val time     = varchar("time", 50)
    val isRead   = bool("is_read").default(false)

    override val primaryKey = PrimaryKey(id)
}
