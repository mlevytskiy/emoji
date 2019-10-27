package com.example.api

import com.example.API_VERSION
import com.example.JwtService
import com.example.api.request.RegistrationRequest
import com.example.api.response.RegistrationResponse
import com.example.hash
import com.example.model.WumfUser
import com.example.redirect
import com.example.repository.Repository
import com.example.repository.WumfRepository
import io.ktor.application.call
import io.ktor.locations.Location
import io.ktor.locations.post
import io.ktor.request.receive
import io.ktor.routing.Route
import io.ktor.http.Parameters
import io.ktor.response.respond
import io.ktor.response.respondText

const val REGISTRATION_ENDPOINT = "/registration"

@Location(REGISTRATION_ENDPOINT)
class Registration

fun Route.registration(db: WumfRepository, jwtService: JwtService) {
    post<Registration> {
        val request = call.receive<RegistrationRequest>()

        var user = db.user(request.userId, hash(request.createdPasswordHash))

        if (user == null) {
            user = WumfUser(request.userId, "SomeName", hash(request.createdPasswordHash))
            db.createUser(user)
            val token = jwtService.generateToken(user)
            call.respond(RegistrationResponse(token))
        } else {
            call.respondText{ "User already exist"}
        }
    }
}