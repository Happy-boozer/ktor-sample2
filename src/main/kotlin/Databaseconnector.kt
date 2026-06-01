package com.example

import com.example.data.tables.Cars
import com.example.data.tables.Notifications
import com.example.data.tables.Users
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

object DatabaseConnector {
    fun init() {
        println("init called")
        val config = HikariConfig().apply {
            jdbcUrl = "jdbc:postgresql://localhost:5432/postgres?currentSchema=public"
            driverClassName = "org.postgresql.Driver"
            username = "postgres"
            password = "2005"
            maximumPoolSize = 10
            minimumIdle = 2
        }

        val dataSource = HikariDataSource(config)
        Database.connect(dataSource)

        transaction {
            SchemaUtils.create(Users, Cars, Notifications)
            seed()
        }
    }

    private fun seed() {
        val userIds: List<Int>

        if (Users.selectAll().count() == 0L) {
            val testUsers = listOf(
                Triple("Иван Иванов", "79001112233", "password1"),
                Triple("Мария Петрова", "79009998877", "password2"),
                Triple("Алексей Смирнов", "79163334455", "password3"),
            )
            userIds = testUsers.mapIndexed { index, (name, phone, password) ->
                val id = index + 1
                Users.insert {
                    it[Users.id] = id
                    it[Users.name] = name
                    it[Users.phone_number] = phone
                    it[Users.password] = PasswordHasherSimple.hashPassword(password)
                }
                id
            }
            println("Seed: inserted ${testUsers.size} users")
        } else {
            userIds = Users.selectAll().map { it[Users.id] }.take(3)
        }

        if (Notifications.selectAll().count() == 0L) {
            data class SeedNotif(val userId: Int, val type: String, val title: String, val body: String, val time: String, val isRead: Boolean)
            val notifs = listOf(
                SeedNotif(userIds[0], "ORDER",    "Заказ выполнен",        "Ваш Ford Focus готов к выдаче. Приезжайте в удобное время.", "Сегодня, 14:32",   false),
                SeedNotif(userIds[0], "REMINDER", "Напоминание о ТО",     "Toyota Camry — пробег подходит к плановому ТО. Осталось ~500 км.", "Сегодня, 10:00", false),
                SeedNotif(userIds[1], "PROMO",    "Акция",                 "Скидка 15% на шиномонтаж до конца месяца. Успейте записаться!", "Вчера, 18:45",    false),
                SeedNotif(userIds[0], "ORDER",    "Диагностика завершена", "Ford Focus: выявлены замечания по тормозной системе.", "Вчера, 12:10",              true),
                SeedNotif(userIds[1], "REMINDER", "Страховка истекает",   "ОСАГО истекает через 14 дней. Не забудьте продлить.", "23 мая, 09:00",             true),
                SeedNotif(userIds[2], "PROMO",    "Новая услуга",          "Теперь доступна замена масла без записи.", "20 мая, 11:00",                         true),
            )
            notifs.forEach { n ->
                Notifications.insert {
                    it[Notifications.userId] = n.userId
                    it[Notifications.type]   = n.type
                    it[Notifications.title]  = n.title
                    it[Notifications.body]   = n.body
                    it[Notifications.time]   = n.time
                    it[Notifications.isRead] = n.isRead
                }
            }
            println("Seed: inserted ${notifs.size} notifications")
        }

        if (Cars.selectAll().count() == 0L) {
            val testCars = listOf(
                listOf(userIds[0], "А123БВ77", "WVWZZZ1KZM056123", "Toyota Camry", "активен"),
                listOf(userIds[0], "В456ГД99", "1HGBH41JXMN10918", "Ford Focus", "активен"),
                listOf(userIds[1], "Е789ЖЗ78", "JF1GH63617H50783", "Kia Rio", "активен"),
                listOf(userIds[2], "К321ЛМ50", "SALVA2BD4EA32652", "Nissan Qashqai", "активен"),
            )
            testCars.forEach { car ->
                Cars.insert {
                    it[Cars.user_id] = car[0] as Int
                    it[Cars.sighn] = car[1] as String
                    it[Cars.vin] = car[2] as String
                    it[Cars.name] = car[3] as String
                    it[Cars.satatus] = car[4] as String
                }
            }
            println("Seed: inserted ${testCars.size} cars")
        }
    }
}