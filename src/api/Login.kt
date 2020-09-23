package com.example.api

import com.example.JwtService
import com.example.api.request.LoginRequest
import com.example.api.response.LoginResponse
import com.example.hash
import com.example.redirect
import com.example.repository.Repository
import com.example.repository.WumfRepository
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.locations.Location
import io.ktor.locations.post
import io.ktor.request.receive
import io.ktor.routing.Route
import io.ktor.http.Parameters
import io.ktor.response.respond
import io.ktor.response.respondText

const val LOGIN_ENDPOINT = "/login"

@Location(LOGIN_ENDPOINT)
class Login

fun Route.login(db: WumfRepository, jwtService: JwtService) {
    post<Login> {
        val request = call.receive<LoginRequest>()

        val user = db.user(request.userId, hash(request.passwordHash))
        if (user != null) {
            val token = jwtService.generateToken(user)
            val friendListStr = jwtService.getFriendsList(request.friendsList, db)
            call.respond(LoginResponse(token = token, friendsList = friendListStr))
        } else {
            call.respond(HttpStatusCode.InternalServerError,"Invalid user")
        }
    }
}