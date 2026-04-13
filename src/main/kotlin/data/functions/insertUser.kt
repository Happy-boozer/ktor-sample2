package com.example.data.functions

import com.example.data.classes.User
import com.example.data.tables.Users
import com.example.data.tables.Users.name
import com.example.data.tables.Users.password
import com.example.data.tables.Users.phone_number
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

fun insertUser(usver: User){
    val newUser = transaction {
        Users.insert {
            it[name] = usver.username
            it[phone_number] = usver.phone_number
            it[password] = usver.password.toString()
        }
    }
}