package com.example.api

import com.example.api.request.CheckRegistrationRequest
import com.example.api.response.CheckRegistrationResponse
import com.example.redirect
import com.example.repository.WumfRepository
import io.ktor.application.call
import io.ktor.locations.Location
import io.ktor.locations.post
import io.ktor.request.receive
import io.ktor.routing.Route
import io.ktor.response.respond

const val CHECK_REGISTRATION_ENDPOINT = "/checkRegistration"

@Location(CHECK_REGISTRATION_ENDPOINT)
class CheckRegistration

fun Route.checkRegistration(db: WumfRepository) {
    post<CheckRegistration> {
        val request = call.receive<CheckRegistrationRequest>()

        var user = db.user(request.userId)

        if (user == null) {
            call.respond ( CheckRegistrationResponse(false) )
        } else {
            call.respond( CheckRegistrationResponse(true) )
        }
    }
}