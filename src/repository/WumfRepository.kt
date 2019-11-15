package com.example.repository

import com.example.model.User
import com.example.model.WumfUser

interface WumfRepository {

    suspend fun createUser(user: WumfUser)
    suspend fun user(userId: Int, hash: String? = null): WumfUser?
    suspend fun user(userId: Int): WumfUser?
    suspend fun users(userIds: List<Int>): List<WumfUser>
    suspend fun addApp(user: WumfUser, appStr: String): String
    suspend fun removeApp(user: WumfUser, appStr: String): String
    suspend fun clearApps(user: WumfUser)
    suspend fun getApps(userId: Int): String
    suspend fun getAllUsers(): List<WumfUser>


}