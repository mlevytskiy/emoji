package com.example.repository

import com.example.model.WumfUser

interface WumfRepository {

    suspend fun createUser(user: WumfUser)
    suspend fun checkUser(userId: Int, hash: String) : Boolean
    suspend fun user(userId: Int): WumfUser?

}