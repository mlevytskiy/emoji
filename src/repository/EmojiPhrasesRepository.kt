package com.example.repository

import com.example.model.EmojiPhrase
import com.example.model.EmojiPhrases
import com.example.model.User
import com.example.model.Users
import com.example.repository.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.lang.IllegalArgumentException

class EmojiPhrasesRepository: Repository {
    override suspend fun userById(userId: String) = dbQuery {
        Users.select { Users.id.eq(userId) }.map { toUser(it) }.singleOrNull()
    }

    override suspend fun user(userId: String, hash: String?): User? {
        val user = dbQuery {
            Users.select {
                (Users.id eq userId)
            }.mapNotNull { toUser(it) }
                .singleOrNull()
        }

        return when {
            user == null -> null
            hash == null -> user
            user.passwordHash == hash -> user
            else -> null
        }
    }

    override suspend fun userByEmail(email: String): User? = dbQuery {
        Users.select { Users.email.eq(email) }
            .map { User(
                it[Users.id],
                email,
                it[Users.displayName],
                it[Users.passwordHash]
            ) }.singleOrNull()
    }

    override suspend fun createUser(user: User) = dbQuery {
        Users.insert {
            it[id] = user.userId
            it[displayName] = user.displayName
            it[email] = user.email
            it[passwordHash] = user.passwordHash
        }
        Unit
    }

    override suspend fun add(userId: String, emojiValue: String, phraseValue: String) = dbQuery {
            val insertStatement = EmojiPhrases.insert {
                it[user] = userId
                it[emoji] = emojiValue
                it[phrase] = phraseValue
            }
            val result = insertStatement.resultedValues?.get(0)
            result?.let {
                toEmojiPhrase(result)
            } ?:run {
                null
            }
    }

    override suspend fun phrase(id: Int): EmojiPhrase? = dbQuery {
        EmojiPhrases.select {
            (EmojiPhrases.id eq id)
        }.mapNotNull { toEmojiPhrase(it) }
            .singleOrNull()
    }

    override suspend fun phrase(id: String): EmojiPhrase? = phrase(id.toInt())

    override suspend fun phrases(id: String): List<EmojiPhrase> = dbQuery {
//        EmojiPhrases.selectAll().map { toEmojiPhrase(it) }
        EmojiPhrases.select { EmojiPhrases.user eq id }.map { toEmojiPhrase(it) }
    }

    override suspend fun remove(id: Int): Boolean {
        if (phrase(id) == null) {
            throw IllegalArgumentException("No phrase found for id $id")
        }
        return dbQuery {
            EmojiPhrases.deleteWhere { EmojiPhrases.id eq id } > 0
        }
    }

    override suspend fun remove(id: String): Boolean = remove(id.toInt())

    override suspend fun clear() {
        EmojiPhrases.deleteAll()
    }

    private fun toEmojiPhrase(row: ResultRow): EmojiPhrase =
        EmojiPhrase(id = row[EmojiPhrases.id].value,
            userId = row[EmojiPhrases.user],
            emoji = row[EmojiPhrases.emoji],
            phrase = row[EmojiPhrases.phrase])

    private fun toUser(row: ResultRow): User =
        User(
            userId = row[Users.id],
            email = row[Users.email],
            displayName = row[Users.displayName],
            passwordHash = row[Users.passwordHash]
        )
}