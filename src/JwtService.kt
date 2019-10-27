package com.example

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.example.model.User
import com.example.model.WumfUser
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

    private fun expiresAt() = Date(System.currentTimeMillis() + 3_600_000 * 24) //24 hour
}