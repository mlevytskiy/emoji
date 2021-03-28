package com.example.repository

import com.example.model.WumfUser
import com.example.neo4j.App
import com.example.neo4j.User

interface Neo4jRepository {

    suspend fun test(): User

    suspend fun getUser(telegramId: Long): User?
    suspend fun createUser(user: User): Boolean
    suspend fun removeUser(telegramId: Long): Boolean

    suspend fun addApp(telegramId: Long, packageName: String): Boolean
    suspend fun removeApp(telegramId: Long, packageName: String): Boolean
    suspend fun getApps(telegramId: Long): List<App>

    suspend fun login(me: Long, myFriends: List<Long>): List<User>
    suspend fun getUsers(users: List<Long>): List<User>

    suspend fun getWorldApps(): List<App>
    suspend fun getCountryApps(country: String): List<App>
    suspend fun getFriendsApps(friends: List<Long>): List<App>

}