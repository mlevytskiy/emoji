package com.example.repository

import com.example.model.App

interface NotMyAppsRepository {

    suspend fun getWorldApps(): List<App>

    suspend fun getCountryApps(country: String): List<App>

    suspend fun getAmongFriendsApps(friends: List<Int>): List<App>

}