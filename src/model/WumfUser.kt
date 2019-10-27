package com.example.model

import com.example.model.Users.primaryKey
import com.example.model.Users.uniqueIndex
import io.ktor.auth.Principal
import org.jetbrains.exposed.sql.Table
import java.io.Serializable

data class WumfUser (
    val telegramId: String,
    val displayName: String,
    val passwordHash: String,
    val apps : String = "") : Serializable, Principal {

}

object WumfUsers: Table() {
    val id = varchar("id", 20).primaryKey()
    val displayName = varchar("display_name", 256)
    val passwordHash = varchar("password_hash", 64)
    var apps = varchar("apps", length = 512)
}