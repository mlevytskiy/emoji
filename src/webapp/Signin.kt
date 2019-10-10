package com.example.webapp

import com.example.MIN_PASSWORD_LENGTH
import com.example.MIN_USER_ID_LENGTH
import com.example.model.EPSession
import com.example.redirect
import com.example.repository.Repository
import com.example.userNameValid
import io.ktor.application.call
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.locations.Location
import io.ktor.locations.get
import io.ktor.locations.post
import io.ktor.request.receive
import io.ktor.request.receiveParameters
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.sessions.sessions

const val SIGNIN = "/signin"

@Location(SIGNIN)
data class Signin(val userId: String = "", val error: String = "")

fun Route.signin(db: Repository, hashFunction: (String) -> String) {
    post<Signin> {
       val signInParameters = call.receiveParameters()
        val userId = signInParameters["userId"] ?: return@post call.redirect(it)
        val password = signInParameters["password"] ?: return@post call.redirect(it)

        val signInError = Signin(userId)

        val signin = when {
            userId.length < MIN_USER_ID_LENGTH -> null
            password.length < MIN_PASSWORD_LENGTH -> null
            !userNameValid(userId) -> null
            else -> db.user(userId, hashFunction(password))
        }

        if (signin == null) {
            call.redirect(signInError.copy(error = "Invalid username or password"))
        } else {
            call.sessions.set("session",EPSession(signin.userId))
            call.redirect(Phrases())
        }
    }
    get<Signin> {
        val user = call.sessions.get("session")?.let { db.user((it as EPSession).userId) }

        if (user != null) {
            call.redirect(Home())
        } else {
            call.respond(FreeMarkerContent("signin.ftl", mapOf("userId" to it.userId, "error" to it.error)))
        }
    }
}