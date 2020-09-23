package com.example

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.example.model.User
import com.example.model.WumfUser
import com.example.repository.WumfRepository
import java.util.*

class JwtService {
    private val issuer = "emojiphrases"
    private val jwtSecret = System.getenv("JWT_SECRET")
    private val algorithm = Algorithm.HMAC512(jwtSecret)

    val verifier: JWTVerifier = JWT
        .require(algorithm)
        .withIssuer(issuer)
        .build()

    fun generateToken(user: WumfUser): String = JWT.create()
        .withSubject("Authentication")
        .withIssuer(issuer)
        .withClaim("id", user.telegramId)
        .withExpiresAt(expiresAt())
        .sign(algorithm)

    suspend fun getFriendsList(friendsListStr: String, db: WumfRepository): String {
        val contactsList = ArrayList<Int>()
        val users = ArrayList<WumfUser>()
        if (friendsListStr.isNotEmpty()) {
            if (friendsListStr.contains(",")) {
                contactsList.addAll(friendsListStr.split(",").map { it.toInt() })
            } else {
                contactsList.add(friendsListStr.toInt())
            }
            users.addAll(db.users(userIds = contactsList))
        }
        var friendListStr = ""
        if (users.isNotEmpty()) {
            friendListStr=users.map { it.telegramId }.joinToString(separator = ",")
        }
        return friendListStr
    }

    private fun expiresAt() = Date(System.currentTimeMillis() + 3_600_000 * 24) //24 hour
}