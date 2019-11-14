package com.example.repository

import com.example.model.User
import com.example.model.WumfUser

interface WumfRepository {

    suspend fun createUser(user: WumfUser)
    suspend fun user(userId: Int, hash: String? = null): WumfUser?
    suspend fun user(userId: Int): WumfUser?
    suspend fun addApp(userId: Int, appStr: String): String
    suspend fun removeApp(userId: Int, appStr: String): String
    suspend fun clearApps(userId: Int)
    suspend fun getApps(userId: Int): String


}