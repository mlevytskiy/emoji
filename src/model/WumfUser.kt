package com.example.model

import io.ktor.auth.Principal
import org.jetbrains.exposed.sql.Table
import java.io.Serializable

data class WumfUser (
    val telegramId: Int,
    val displayName: String,
    val passwordHash: String,
    var apps : String = "",
    val country: String) : Serializable, Principal {

}

object WumfUsers: Table() {
    val id = integer("id").primaryKey()
    val displayName = varchar("display_name", 256)
    val passwordHash = varchar("password_hash", 64)
    var apps = varchar("apps", length = 512)
    val country = varchar("country", length = 4)
}