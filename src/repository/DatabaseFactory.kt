package com.example.repository

import com.example.model.EmojiPhrases
import com.example.model.Users
import com.example.model.WumfUser
import com.example.model.WumfUsers
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {

    fun init(countryUsers: HashMap<String, MutableList<WumfUser>>, repository: WumfRepository) {
        Database.connect(hikari())

        transaction {
            SchemaUtils.create(EmojiPhrases)
            SchemaUtils.create(Users)
            SchemaUtils.create(WumfUsers)
            runBlocking {
                System.out.println("start fill countryUsers")
                val users = repository.getAllUsers()
                users.forEach {it
                    countryUsers[it.country]?.add(it)?:run{ countryUsers[it.country] = mutableListOf(it) }
                }
                System.out.println("countryUsers is Empty?" + countryUsers.isEmpty())
            }
        }
    }

    private fun hikari(): HikariDataSource {
        val config = HikariConfig()
        config.driverClassName = "org.postgresql.Driver"
        config.jdbcUrl = System.getenv("JDBC_DATABASE_URL")
        config.maximumPoolSize = 3
        config.isAutoCommit = false
        config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        config.validate()
        return HikariDataSource(config)
    }

    suspend fun <T> dbQuery(
        block: () -> T): T =
        withContext(Dispatchers.IO) {
            transaction { block() }
        }

}