package com.example.data.repository

import com.example.PasswordHasherSimple
import com.example.data.tables.Users
import com.example.domain.model.User
import com.example.domain.repository.UserRepository
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.experimental.newSuspendedTransaction

class UserRepositoryImpl : UserRepository {

    override suspend fun findByPhone(phone: String): User? = newSuspendedTransaction {
        Users.selectAll()
            .where { Users.phone_number eq phone }
            .map { it.toUser() }
            .firstOrNull()
    }

    override suspend fun findByPhoneAndPassword(phone: String, password: String): User? = newSuspendedTransaction {
        Users.selectAll()
            .where { Users.phone_number eq phone }
            .map { it.toUser() }
            .firstOrNull()
            ?.takeIf { PasswordHasherSimple.verifyPassword(password, it.password) }
    }

    override suspend fun insert(user: User): Unit = newSuspendedTransaction {
        Users.insert {
            it[name] = user.name
            it[phone_number] = user.phoneNumber
            it[password] = user.password
        }
    }

    private fun org.jetbrains.exposed.v1.core.ResultRow.toUser() = User(
        id          = this[Users.id],
        name        = this[Users.name],
        phoneNumber = this[Users.phone_number],
        password    = this[Users.password]
    )
}
