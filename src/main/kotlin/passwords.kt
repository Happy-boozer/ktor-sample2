package com.example
import org.mindrot.jbcrypt.BCrypt


object PasswordHasherSimple {

    fun hashPassword(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }

    fun verifyPassword(password: String?, hashedPassword: String): Boolean {
        return BCrypt.checkpw(password, hashedPassword)
    }


}

fun main() {
    val hashed = PasswordHasherSimple.hashPassword("MyPassword123")
    println("BCrypt хэш: $hashed")

    val ha = ""

    val isValid = PasswordHasherSimple.verifyPassword("MyPassword123", hashed)
    println("Пароль верный: $isValid")
}