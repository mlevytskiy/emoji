package com.example.repository

import com.example.model.WumfUser
import com.example.model.WumfUsers
import org.jetbrains.exposed.sql.*

class WumfUsersRepository: WumfRepository {

    override suspend fun createUser(user: WumfUser) = DatabaseFactory.dbQuery {
        WumfUsers.insert {
            it[id] = user.telegramId
            it[passwordHash] = user.passwordHash
        }
        Unit
    }

    override suspend fun checkUser(userId: Int, hash: String): Boolean {
        val user = user(userId)
        return when {
            user == null -> false
            user.passwordHash == hash -> true
            else -> false
        }
    }

    override suspend fun user(userId: Int): WumfUser? = DatabaseFactory.dbQuery {
        WumfUsers.select {
            (WumfUsers.id eq userId)
        }.mapNotNull { toUser(it) }
            .singleOrNull()
    }

    private fun toUser(row: ResultRow): WumfUser =
        WumfUser(
            telegramId = row[WumfUsers.id],
            passwordHash = row[WumfUsers.passwordHash]
        )
}
