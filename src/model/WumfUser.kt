package com.example.model

import io.ktor.auth.Principal
import org.jetbrains.exposed.sql.Table
import java.io.Serializable

data class WumfUser (
    val telegramId: Int,
    val passwordHash: String) : Serializable, Principal {

}

object WumfUsers: Table() {
    val id = integer("id").primaryKey()
    val passwordHash = varchar("password_hash", 64)
}