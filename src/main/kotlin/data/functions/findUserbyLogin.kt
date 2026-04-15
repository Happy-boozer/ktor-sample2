package com.example.data.functions

import com.example.data.classes.User
import com.example.data.tables.Users
import com.example.PasswordHasherSimple.verifyPassword
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.experimental.newSuspendedTransaction

fun check(password: String?, hashed: String): String?{
    if(verifyPassword(password, hashed)){
        return password
    }
    else{
        return null
    }
}

suspend fun UserbyLoginAndPassword(Login: String, password: String?): User? = newSuspendedTransaction {
    Users.selectAll()
        .where { Users.phone_number eq Login }.map{
        User(
            id = it[Users.id],
            username = it[Users.name],
            phone_number = it[Users.phone_number],
            password = check(password, it[Users.password])
        )
    }.firstOrNull()
}

suspend fun UserbyLoginId(Login: String): User? = newSuspendedTransaction {
    Users.selectAll()
        .where { Users.phone_number eq Login }.map{
            User(
                id = it[Users.id],
                username = it[Users.name],
                phone_number = it[Users.phone_number],
                password = it[Users.password]
            )
        }.firstOrNull()
}