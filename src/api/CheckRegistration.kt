package com.example.api

import com.example.api.request.CheckRegistrationRequest
import com.example.api.response.CheckRegistrationResponse
import com.example.repository.Neo4jRepository
import io.ktor.application.call
import io.ktor.locations.Location
import io.ktor.locations.post
import io.ktor.request.receive
import io.ktor.routing.Route
import io.ktor.response.respond

const val CHECK_REGISTRATION_ENDPOINT = "/checkRegistration"

@Location(CHECK_REGISTRATION_ENDPOINT)
class CheckRegistration

//fun Route.checkRegistration(db: WumfRepository) {
//    post<CheckRegistration> {
//        val request = call.receive<CheckRegistrationRequest>()
//
//        var user = db.user(request.userId)
//
//        if (user == null) {
//            call.respond ( CheckRegistrationResponse(false) )
//        } else {
//            call.respond( CheckRegistrationResponse(true) )
//        }
//    }
//}

fun Route.checkRegistration(db: Neo4jRepository) {
    post<CheckRegistration> {
        val request = call.receive<CheckRegistrationRequest>()
        val user = db.getUser(request.userId.toLong())

        if (user == null) {
            call.respond ( CheckRegistrationResponse(false) )
        } else {
            call.respond( CheckRegistrationResponse(true) )
        }
    }
}

//val request = call.receive<RegistrationRequest>()
//val result = db.getUser(request.userId.toLong())
//if (result == null) {
//    val token = jwtService.generateToken(request.userId)
//    call.respond(RegistrationResponse(token = token, friendsList = emptyList()))
//} else {
//    call.respond(HttpStatusCode.InternalServerError, "User already exist")
//}