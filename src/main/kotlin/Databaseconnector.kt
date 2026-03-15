package com.example

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.v1.jdbc.Database
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

    }
}