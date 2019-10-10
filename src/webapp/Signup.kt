package com.example.webapp

import com.example.MIN_PASSWORD_LENGTH
import com.example.MIN_USER_ID_LENGTH
import com.example.model.EPSession
import com.example.model.User
import com.example.redirect
import com.example.repository.Repository
import com.example.userNameValid
import io.ktor.application.application
import io.ktor.application.call
import io.ktor.application.log
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.locations.Location
import io.ktor.locations.get
import io.ktor.locations.post
import io.ktor.request.receiveParameters
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.sessions.get
import io.ktor.sessions.sessions

const val SIGNUP = "/signup"

@Location(SIGNUP)
data class Signup(
    val userId: String = "",
    val displayName: String = "",
    val email: String = "",
    val error: String = "")

fun Route.signup(db: Repository, hashFunction: (String) -> String) {
    post<Signup> {
        val user = call.sessions.get<EPSession>()?.let { db.user(it.userId) }
        if (user != null) return@post call.redirect(Phrases())

        val signupParameters = call.receiveParameters()
        System.out.println("userId=" + signupParameters["userId"])
        val userId = signupParameters["userId"] ?: return@post call.redirect(it)
        System.out.println("userId2=" + userId)
        val password = signupParameters["password"] ?: return@post call.redirect(it)
        System.out.println("password=" + password)
        val displayName = signupParameters["displayName"] ?: return@post call.redirect(it)
        System.out.println("displayName=" + displayName)
        val email = signupParameters["email"] ?: return@post call.redirect(it)
        System.out.println("email=" + email)

        val signUpError = Signup(userId, displayName, email)

        when {
            password.length < MIN_PASSWORD_LENGTH ->
                call.redirect(signUpError.copy(error = "Password should be at least $MIN_PASSWORD_LENGTH characters long"))
            userId.length < MIN_USER_ID_LENGTH ->
                call.redirect(signUpError.copy(error = "Username should be at least $MIN_USER_ID_LENGTH characters long"))
            !userNameValid(userId) ->
                call.redirect(signUpError.copy(error = "Username should consist of digits, letters, dots or underscores"))
            db.user(userId) != null ->
                call.redirect(signUpError.copy(error = "User with the fallowing username is already registered"))
            else -> {
                val hash = hashFunction(password)
                val newUser = User(userId, email, displayName, hash)

                try {
                    db.createUser(newUser)
                } catch (e: Throwable) {
                    when {
                        db.user(userId) != null ->
                            call.redirect(signUpError.copy(error="User with the fallowing username $displayName already registered"))
                        db.userByEmail(email) != null ->
                            call.redirect(signUpError.copy(error="User with the fallowing email $email is already registered"))
                        else -> {
                            application.log.error("Failed to register user", e)
                            call.redirect(signUpError.copy(error = "Failed to register user"))
                        }
                    }
                }

                call.sessions.set("session", EPSession(newUser.userId))
                call.redirect(Phrases())


            }
        }


    }
    get<Signup> {
        val user = call.sessions.get("session")?.let { db.user((it as EPSession).userId) }
        if (user != null) {
            call.redirect(Phrases())
        } else {
            call.respond(FreeMarkerContent("signup.ftl", mapOf("error" to it.error)))
        }
    }
}