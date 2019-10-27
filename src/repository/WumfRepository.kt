package com.example.repository

import com.example.model.User
import com.example.model.WumfUser

interface WumfRepository {

    suspend fun createUser(user: WumfUser)
    suspend fun user(userId: String, hash: String? = null): WumfUser?
    suspend fun user(userId: String): WumfUser?
    suspend fun addApp(userId: String, appStr: String): String
    suspend fun removeApp(userId: String, appStr: String): String
    suspend fun clearApps(userId: String)
    suspend fun getApps(userId: String): String


}