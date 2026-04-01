//import org.gradle.internal.impldep.com.jcraft.jsch.jbcrypt.BCrypt
import org.mindrot.jbcrypt.BCrypt


object PasswordHasherSimple {

    /**
     * Хэширует пароль с использованием BCrypt
     */
    fun hashPassword(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }

    /**
     * Проверяет пароль
     */
    fun verifyPassword(password: String, hashedPassword: String): Boolean {
        return BCrypt.checkpw(password, hashedPassword)
    }
}

// Пример использования
/*fun main() {
    val hashed = PasswordHasherSimple.hashPassword("MyPassword123")
    println("BCrypt хэш: $hashed")

    val isValid = PasswordHasherSimple.verifyPassword("MyPassword123", hashed)
    println("Пароль верный: $isValid")
}*/