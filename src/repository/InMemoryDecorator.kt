package com.example.repository

import com.example.model.App
import com.example.model.WumfUser

class InMemoryDecorator(private val repository: WumfRepository,
                        private val countryUsers: HashMap<String, MutableList<WumfUser>>) : WumfRepository, NotMyAppsRepository {

    init {
        System.out.println("InMemoryDecorator init")
    }

    override suspend fun users(userIds: List<Int>): List<WumfUser> {
        return repository.users(userIds)
    }

    override suspend fun getWorldApps(): List<App> {
        val map = HashMap<String, MutableList<Int>>()
        for (i in countryUsers.entries) {
            if (i.value.isNullOrEmpty()) {
                fillApps(i.value, map)
            }
        }
        return convertToApps(map)
    }

    override suspend fun getCountryApps(country: String): List<App> {

        countryUsers[country]?.let {
            val map = HashMap<String, MutableList<Int>>()
            fillApps(it, map)
            return convertToApps(map)
        }

        return emptyList()
    }

    private fun convertToApps(map: HashMap<String, MutableList<Int>>): List<App> {
        val result = ArrayList<App>()
        map.entries.forEach {
            result.add(App(it.key, it.value))
        }
        return result
    }

    private fun fillApps(users: MutableList<WumfUser>, map: HashMap<String, MutableList<Int>>) {
        for (i in users) {
            fillApps(i, map)
        }
    }

    private fun fillApps(user: WumfUser, map: HashMap<String, MutableList<Int>>) {
        val appsPackages =  user.apps.split(",")
        for (i in appsPackages) {
            map[i]?.plus(user.telegramId) ?:run {
                map[i] = arrayListOf(user.telegramId)
            }
        }
    }

    override suspend fun getAmongFriendsApps(friends: List<Int>): List<App> {
        val users = repository.users(friends)
        val map = HashMap<String, MutableList<Int>>()
        users.forEach {
            fillApps(it, map)
        }
        return convertToApps(map)
    }

    override suspend fun createUser(user: WumfUser) {
        repository.createUser(user)
        val users = countryUsers[user.country] ?: ArrayList()
        users.add(user)
        countryUsers[user.country] = users
    }

    override suspend fun user(userId: Int, hash: String?): WumfUser? {
        return repository.user(userId, hash)
    }

    override suspend fun user(userId: Int): WumfUser? {
        return repository.user(userId)
    }

    override suspend fun addApp(user: WumfUser, appStr: String): String {
        val apps = repository.addApp(user, appStr)
        val users = countryUsers[user.country]
        users?.let {
            for (i in 0 until users.size) {
                if (users[i].telegramId == user.telegramId) {
                    users[i].apps = apps
                }
            }
        } ?:run {
            user.apps = apps
            countryUsers[user.country] = arrayListOf(user)
        }

        return apps
    }

    override suspend fun removeApp(user: WumfUser, appStr: String): String {
        val apps = repository.removeApp(user, appStr)
        val users = countryUsers[user.country]
        users?.let {
            for (i in 0 until users.size) {
                if (users[i].telegramId == user.telegramId) {
                    users[i].apps = apps
                }
            }
        } ?:run {
            user.apps = apps
            countryUsers[user.country] = arrayListOf(user)
        }
        return apps
    }

    override suspend fun clearApps(user: WumfUser) {
        repository.clearApps(user)
        val users = countryUsers[user.country]
        for (i in 0 until (users?.size ?: 0)) {
            if (users!![i].telegramId == user.telegramId) {
                users[i].apps = ""
            }
        }
    }

    override suspend fun getApps(userId: Int): String {
        return repository.getApps(userId)
    }

    override suspend fun getAllUsers(): List<WumfUser> {
        return repository.getAllUsers()
    }

}