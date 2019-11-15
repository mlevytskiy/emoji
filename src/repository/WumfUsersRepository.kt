package com.example.repository

import com.example.model.WumfUser
import com.example.model.WumfUsers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class WumfUsersRepository: WumfRepository {

    override suspend fun clearApps(userId: Int) {
        DatabaseFactory.dbQuery {
            WumfUsers.update({WumfUsers.id eq userId}) {
                it[apps] = ""
            }
        }
    }

    override suspend fun getApps(userId: Int): String {
        user(userId)?.let{
            return it.apps
        }
        return ""
    }

    override suspend fun removeApp(userId: Int, appStr: String): String {
        var result = ""
        withContext(Dispatchers.IO) {
            transaction {
                val user = WumfUsers.select {
                    (WumfUsers.id eq userId)
                }.mapNotNull { toUser(it) }.singleOrNull()
                user?.let {
                    val oldApps = it.apps
                    result = removeAppStr(oldApps, appStr)
                    WumfUsers.update({WumfUsers.id eq userId}) {
                        it[apps] = result
                    }
                }
            }
        }
        return result
    }

    override suspend fun addApp(userId: Int, appStr: String): String {
        var result = ""
        withContext(Dispatchers.IO) {
            transaction {
                val user = WumfUsers.select {
                    (WumfUsers.id eq userId)
                }.mapNotNull { toUser(it) }.singleOrNull()
                user?.let {
                    val oldApps = it.apps
                    result = addAppStr(oldApps, appStr)
                    WumfUsers.update({WumfUsers.id eq userId}) {
                        it[apps] = result
                    }
                }
            }
        }
        return result
    }

    private fun removeAppStr(oldApps: String, appForRemove: String): String {
        val listApps = getAllApps(oldApps)
        return if (listApps.contains(appForRemove)) {
            listApps.remove(appForRemove)
            arrayToStr(listApps)
        } else {
            oldApps
        }
    }

    private fun addAppStr(oldApps: String, newApp: String): String {
        val listApps = getAllApps(oldApps)
        return if (listApps.contains(newApp)) {
            oldApps
        } else {
            listApps.add(newApp)
            arrayToStr(listApps)
        }
    }

    private fun getAllApps(appsStr: String): MutableList<String> {
        return appsStr.split(",").toMutableList()
    }

    private fun arrayToStr(list: List<String>): String {
        if (list.isEmpty()) {
            return ""
        } else {
            return list.joinToString(separator=",")
        }
    }

    override suspend fun createUser(user: WumfUser) = DatabaseFactory.dbQuery {
        WumfUsers.insert {
            it[id] = user.telegramId
            it[displayName] = user.displayName
            it[passwordHash] = user.passwordHash
            it[apps] = user.apps
            it[country] = user.country
        }
        Unit
    }

    override suspend fun user(userId: Int, hash: String?): WumfUser? {
        val user = user(userId)

        return when {
            user == null -> null
            hash == null -> user
            user.passwordHash == hash -> user
            else -> null
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
            displayName = row[WumfUsers.displayName],
            passwordHash = row[WumfUsers.passwordHash],
            apps = row[WumfUsers.apps],
            country = row[WumfUsers.country]
        )

    override suspend fun getAllUsers(): List<WumfUser> {
        return WumfUsers.selectAll().mapNotNull { toUser(it) }
    }
}
